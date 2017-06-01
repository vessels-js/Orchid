

import com.subgraph.orchid.http.TorRequest;

public class Grab {
    public static void main(String[] args) throws Exception {
        try{
            TorRequest.openTunnel();
            TorRequest whatIsMyIp = TorRequest.getInstance("http://ip-api.com/json");
            whatIsMyIp.executeRequest();
            System.out.println(whatIsMyIp.getResponse().getContent());
        } finally{
            TorRequest.closeTunnel();
        }
    }
}