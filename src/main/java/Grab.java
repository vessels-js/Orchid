

import com.subgraph.orchid.http.TorRequest;
import com.subgraph.orchid.logging.Logger;
import com.subgraph.orchid.logging.SysLog;

public class Grab {
    private static final Logger logger = Logger.getInstance(Grab.class);
    
    public static void main(String[] args) throws Exception {
        try{
            Logger.setLoggingThreshold(SysLog.INFORMATIONAL);
            TorRequest.openTunnel();
            TorRequest whatIsMyIp = TorRequest.getInstance("http://ip-api.com/json");
            whatIsMyIp.executeRequest();
            logger.info(whatIsMyIp.getResponse().getContent());
        } finally{
            TorRequest.closeTunnel();
        }
    }
}