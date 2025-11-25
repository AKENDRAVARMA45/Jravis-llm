import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        System.out.println("🤖 JARVIS AI Core running at http://localhost:8080/chat");
        server.createContext("/chat", new ChatHandler());
        server.setExecutor(null);
        server.start();
    }

    static class ChatHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);
            String message = params.getOrDefault("message", "Hello");

            String reply = getAIResponse(message);

            String jsonResponse = String.format("{\"reply\": \"%s\"}", reply.replace("\"", "\\\""));

            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }

        private Map<String, String> parseQuery(String query) throws IOException {
            Map<String, String> map = new HashMap<>();
            if (query == null) return map;
            for (String pair : query.split("&")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    map.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                            URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
                }
            }
            return map;
        }

        private String getAIResponse(String msg) {
            msg = msg.toLowerCase();
            if (msg.contains("hello")) return "Hello Sir, JARVIS online and fully operational.";
            if (msg.contains("hi")) return "Hi Sir, JARVIS online and fully operational.";
            if (msg.contains("hey")) return "Hey, This is JARVIS online and fully operational.";
            if (msg.contains("who are you")) return "I am J.A.R.V.I.S — your personal AI assistant.";
            if (msg.contains("What is your name")) return "I am J.A.R.V.I.S — your personal AI assistant.";
            if (msg.contains("time")) return "The system time is: " + java.time.LocalTime.now().toString();
            if (msg.contains("date")) return "Today's date is: " + java.time.LocalDate.now().toString();
            if (msg.contains("who is your creator")) return "I was created by Akendra, genius.";
            return "I'm still learning, Sir. Could you rephrase that?";
        }
    }
}
