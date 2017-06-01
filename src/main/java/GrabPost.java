

import com.subgraph.orchid.http.NameValuePair;
import com.subgraph.orchid.http.HttpHeader;
import com.subgraph.orchid.http.TorRequest;
import java.util.*;

public class GrabPost {

    public static void main(String[] args) throws Exception {
        try{
            String url = "http://putsreq.com/NOFo0RJDXusEi760rmrE";
            List<NameValuePair> params = new ArrayList();
            params.add(NameValuePair.getInstance("name","tor"));

            TorRequest.openTunnel();
            TorRequest request = TorRequest.getInstance(url, params);
            request.setMaxRetryAttempts(50);
            request.executeRequest();
            for(HttpHeader header : request.getResponse().getHeaders()){
                System.out.println(header.getName()+"="+header.getValue());
            }
            System.out.println(request.getResponse().getContent());
            System.out.println(request.getResponse().getStatusLine().getStatusCode());
        } finally{
            TorRequest.closeTunnel();
        }
    }
}