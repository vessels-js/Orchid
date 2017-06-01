

import com.subgraph.orchid.http.TorRequest;

public class GrabSSL {
    public static void main(String[] args) throws Exception {
        try{
            TorRequest.openTunnel();
            TorRequest whatIsMyIpSSL = TorRequest.getInstance("https://wtfismyip.com/json");
            whatIsMyIpSSL.executeRequest();
            System.out.println(whatIsMyIpSSL.getResponse().getContent());
        } finally{
            TorRequest.closeTunnel();
        }
    }
}