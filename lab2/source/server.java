import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(4080), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String quary = t.getRequestURI().getQuery();
            Map<String,String> queryMap = queryToMap(quary);
            
            String response = "";
            if(queryMap == null) {
            	response += "Hello World from java!\n";
            } else if (queryMap.get("cmd").equals("time")) {
            	LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
		response+=myDateObj.format(myFormatObj)+"\n";
            }
            
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
	    System.out.println("Served hello world...");
        }
    }
    
    static public Map<String, String> queryToMap(String query) {
	    if(query == null) {
		return null;
	    }
	    Map<String, String> result = new HashMap<>();
	    for (String param : query.split("&")) {
		String[] entry = param.split("=");
		if (entry.length > 1) {
		    result.put(entry[0], entry[1]);
		}else{
		    result.put(entry[0], "");
		}
	    }
	    return result;
    }

}
