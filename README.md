Orchid
======

Orchid is a Tor client implementation and library written in pure Java.

It was written from the Tor specification documents, which are available [here](https://www.torproject.org/docs/documentation.html.en#DesignDoc).

![Orchid](https://subgraph.com/img/orchidlogo1.png)

## Example

'''
2018-04-06 04:06:10,458 INFO - TorClient - Starting Orchid (version: 1.0.0)
2018-04-06 04:06:10,461 INFO - DirectoryImpl - Loading cached network information from disk
2018-04-06 04:06:10,461 INFO - DirectoryImpl - Loading certificates
2018-04-06 04:06:10,469 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:06:15,568 INFO - DirectoryImpl - Loading certificates
2018-04-06 04:06:15,582 INFO - DirectoryImpl - Loading consensus
2018-04-06 04:06:15,615 WARN - DirectoryImpl - Unable to verify signatures on co
nsensus document, discarding...
2018-04-06 04:06:15,615 INFO - DirectoryImpl - Loading microdescriptor cache
2018-04-06 04:06:15,616 INFO - CircuitCreationTask - Cannot build circuits becau
se we don't have enough directory information
2018-04-06 04:06:15,616 INFO - DirectoryImpl - loading state file
2018-04-06 04:06:15,617 INFO - DirectoryDownloadTask - Downloading consensus bec
ause we have no consensus document
2018-04-06 04:06:15,713 INFO - TorClient - >>> [ 5% ]: Connecting to directory s
erver
2018-04-06 04:06:15,947 INFO - TorClient - >>> [ 10% ]: Finishing handshake with
 directory server
2018-04-06 04:07:05,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:07:55,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:08:45,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:09:35,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:10:12,024 INFO - TorClient - >>> [ 15% ]: Establishing an encrypted directory connection
2018-04-06 04:10:12,718 INFO - TorClient - >>> [ 20% ]: Asking for networkstatus consensus
2018-04-06 04:10:13,014 INFO - TorClient - >>> [ 25% ]: Loading networkstatus consensus
2018-04-06 04:10:17,877 INFO - ConsensusDocumentImpl - Certificates need to be retrieved to verify consensus
2018-04-06 04:10:25,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:11:15,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:12:05,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:12:55,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:13:45,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:14:35,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:15:25,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:16:15,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:17:05,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:17:55,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:18:45,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:19:35,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:20:25,577 INFO - CircuitCreationTask - Cannot build circuits because we don't have enough directory information
2018-04-06 04:20:25,616 INFO - TorClient - >>> [ 35% ]: Asking for authority key certs
2018-04-06 04:20:25,900 INFO - TorClient - >>> [ 40% ]: Loading authority key certs
2018-04-06 04:20:31,612 INFO - TorClient - >>> [ 45% ]: Asking for relay descriptors
2018-04-06 04:20:31,662 INFO - TorClient - >>> [ 50% ]: Loading relay descriptors
2018-04-06 04:20:35,680 INFO - TorClient - >>> [ 80% ]: Connecting to the Tor network
2018-04-06 04:20:35,804 INFO - TorClient - >>> [ 85% ]: Finished Handshake with first hop
2018-04-06 04:28:14,594 INFO - TorClient - >>> [ 90% ]: Establishing a Tor circuit
2018-04-06 04:28:15,362 INFO - TorClient - >>> [ 100% ]: Done
2018-04-06 04:28:15,362 INFO - TorClient - Tor is ready to go!

'''