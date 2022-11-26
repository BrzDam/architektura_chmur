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
            } else if (queryMap.get("str").length() != 0) {
            	String str = queryMap.get("str");
		response += analyzeStr(str);
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
    
    static public String analyzeStr(String str) {
        Integer upper = 0, lower = 0, number = 0, special = 0;
 
        for(int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            if (ch >= 'A' && ch <= 'Z')
                upper++;
            else if (ch >= 'a' && ch <= 'z')
                lower++;
            else if (ch >= '0' && ch <= '9')
                number++;
            else
                special++;
        }
 
        System.out.println("Lower case letters : " + lower);
        System.out.println("Upper case letters : " + upper);
        System.out.println("Number : " + number);
        System.out.println("Special characters : " + special);
        
        return  "{\"lowercase\":" + lower.toString() + ",\"uppercase\":" + upper.toString() + ",\"digits\":" + number.toString() + ",\"special\":" + special.toString() + "}";
    }

}
