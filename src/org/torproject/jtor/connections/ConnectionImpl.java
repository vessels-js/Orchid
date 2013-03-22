package org.torproject.jtor.connections;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.net.ssl.SSLSocket;

import org.torproject.jtor.TorException;
import org.torproject.jtor.circuits.Circuit;
import org.torproject.jtor.circuits.Connection;
import org.torproject.jtor.circuits.ConnectionFailedException;
import org.torproject.jtor.circuits.ConnectionHandshakeException;
import org.torproject.jtor.circuits.ConnectionIOException;
import org.torproject.jtor.circuits.ConnectionTimeoutException;
import org.torproject.jtor.circuits.cells.Cell;
import org.torproject.jtor.circuits.impl.CellImpl;
import org.torproject.jtor.crypto.TorRandom;
import org.torproject.jtor.directory.Router;
import org.torproject.jtor.logging.Logger;

/**
 * This class represents a transport link between two onion routers or
 * between an onion proxy and an entry router.
 *
 */
public class ConnectionImpl implements Connection {

	private final static int DEFAULT_CONNECT_TIMEOUT = 5000;

	private final SSLSocket socket;
	private final Logger logger;
	private InputStream input;
	private OutputStream output;
	private final Router router;
	private final Map<Integer, Circuit> circuitMap;
	private final BlockingQueue<Cell> connectionControlCells;
	private int currentId = 1;
	private boolean isConnected;
	private volatile boolean isClosed;
	private final Thread readCellsThread;
	private final Object connectLock = new Object();

	public ConnectionImpl(Logger logger, SSLSocket socket, Router router) {
		this.logger = logger;
		this.socket = socket;
		this.router = router;
		this.circuitMap = new HashMap<Integer, Circuit>();
		this.readCellsThread = new Thread(createReadCellsRunnable());
		this.readCellsThread.setDaemon(true);
		this.connectionControlCells = new LinkedBlockingQueue<Cell>();
		initializeCurrentCircuitId();
	}
	
	private void initializeCurrentCircuitId() {
		final TorRandom random = new TorRandom();
		currentId = random.nextInt(0xFFFF) + 1;
	}

	public Router getRouter() {
		return router;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public int allocateCircuitId(Circuit circuit) {
		synchronized(circuitMap) {
			while(circuitMap.containsKey(currentId)) 
				incrementNextId();
			circuitMap.put(currentId, circuit);
			return currentId;
		}
	}

	private void incrementNextId() {
		currentId++;
		if(currentId > 0xFFFF)
			currentId = 1;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void connect() throws ConnectionFailedException, ConnectionTimeoutException, ConnectionHandshakeException {
		synchronized (connectLock) {
			if(isConnected) {
				return;
			}
			try {
				doConnect();
			} catch (SocketTimeoutException e) {
				throw new ConnectionTimeoutException();
			} catch (IOException e) {
				throw new ConnectionFailedException(e.getClass().getName() + " : "+ e.getMessage());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new ConnectionHandshakeException("Handshake interrupted");
			} catch (ConnectionIOException e) {
				throw new ConnectionFailedException(e.getMessage());
			}
			isConnected = true;
		}
	}

	private void doConnect() throws IOException, InterruptedException, ConnectionIOException {
		final ConnectionHandshakeV2 handshake = new ConnectionHandshakeV2(this, socket);
		socket.connect(routerToSocketAddress(router), DEFAULT_CONNECT_TIMEOUT);
		input = socket.getInputStream();
		output = socket.getOutputStream();
		readCellsThread.start();
		handshake.runHandshake();		
	}

	private SocketAddress routerToSocketAddress(Router router) {
		final InetAddress address = router.getAddress().toInetAddress();
		return new InetSocketAddress(address, router.getOnionPort());
	}

	public void sendCell(Cell cell) throws ConnectionIOException  {
		if(!socket.isConnected()) {
			throw new ConnectionIOException("Cannot send cell because connection is not connected");
		}
		synchronized(output) {
			try {
				output.write(cell.getCellBytes());
			} catch (IOException e) {
				closeSocket();
				// manager.removeActiveConnection(this);
				throw new ConnectionIOException(e.getClass().getName() + " : "+ e.getMessage());
			}
		}
	}

	private Cell recvCell() throws ConnectionIOException {
		try {
			return CellImpl.readFromInputStream(input);
		} catch (IOException e) {
			if(!isClosed) {
				closeSocket();
			}
			// manager.removeActiveConnection(this);
			throw new ConnectionIOException(e.getClass().getName() + " " + e.getMessage());
		}
	}

	private void closeSocket() {
		try {
			isClosed = true;
			socket.close();
			isConnected = false;
			
		} catch (IOException e) {
			logger.warning("Error closing socket: "+ e.getMessage());
		}
	}

	private Runnable createReadCellsRunnable() {
		return new Runnable() {
			public void run() {
				try {
					readCellsLoop();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	private void readCellsLoop() {
		while(!Thread.interrupted()) {
			try {
				processCell( recvCell() );
			} catch(ConnectionIOException e) {
				notifyCircuitsLinkClosed();
				return;
			} catch(TorException e) {
				logger.warning("Unhandled Tor exception reading and processing cells: "+ e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void notifyCircuitsLinkClosed() {
		
	}

	Cell readConnectionControlCell() throws ConnectionIOException {
		try {
			return connectionControlCells.take();
		} catch (InterruptedException e) {
			closeSocket();
			throw new ConnectionIOException();
		}
	}

	private void processCell(Cell cell) {
		final int command = cell.getCommand();

		if(command == Cell.RELAY) {
			processRelayCell(cell);
			return;
		}
		switch(command) {
		case Cell.NETINFO:
		case Cell.VERSIONS:
			connectionControlCells.add(cell);
			break;

		case Cell.CREATED:
		case Cell.CREATED_FAST:
			processControlCell(cell);
			break;
		case Cell.DESTROY:
			processDestroyCell(cell);
			break;

		default:
			// Ignore everything else
			break;
		}
	}

	private void processRelayCell(Cell cell) {
		synchronized(circuitMap) {
			final Circuit circuit = circuitMap.get(cell.getCircuitId());
			if(circuit == null) {
				logger.warning("Could not deliver relay cell for circuit id = "+ cell.getCircuitId() +" on connection "+ this +". Circuit not found");
				return;
			}
			circuit.deliverRelayCell(cell);
		}
	}

	private void processControlCell(Cell cell) {
		synchronized(circuitMap) {
			final Circuit circuit = circuitMap.get(cell.getCircuitId());
			if(circuit == null) {
				logger.warning("Could not deliver control cell for circuit id = "+ cell.getCircuitId() +" on connection "+ this +". Circuit not found");
				return;
			}
			circuit.deliverControlCell(cell);
		}
	}

	private void processDestroyCell(Cell cell) {
		logger.debug("DESTROY cell received ("+ CellImpl.errorToDescription(cell.getByte() & 0xFF) +")");
		synchronized(circuitMap) {
			final Circuit circuit = circuitMap.remove(cell.getCircuitId());
			if(circuit == null)
				return;
			circuit.destroyCircuit();
		}
	}

	public void removeCircuit(Circuit circuit) {
		synchronized(circuitMap) {
			circuitMap.remove(circuit.getCircuitId());
			if(circuitMap.size() == 0) {
				closeSocket();
			}
		}
	}

	public String toString() {
		return "!" + router.getNickname() + "!";
	}
}