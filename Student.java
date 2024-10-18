import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Student {
    private static Map<String, String> students = new HashMap<>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/students", new StudentHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8000");
    }

    static class StudentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "";
            switch (t.getRequestMethod()) {
                case "GET":
                    response = handleGet(t);
                    break;
                case "POST":
                    response = handlePost(t);
                    break;
                case "PUT":
                    response = handlePut(t);
                    break;
                case "DELETE":
                    response = handleDelete(t);
                    break;
                default:
                    response = "Invalid request method";
            }
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String handleGet(HttpExchange t) {
            String studentId = t.getRequestURI().getPath().replace("/students/", "");
            if (students.containsKey(studentId)) {
                return students.get(studentId);
            } else {
                return "Student not found";
            }
        }

        private String handlePost(HttpExchange t) {
            String studentId = t.getRequestURI().getPath().replace("/students/", "");
            String studentName = t.getRequestBody().toString();
            students.put(studentId, studentName);
            return "Student added successfully";
        }

        private String handlePut(HttpExchange t) {
            String studentId = t.getRequestURI().getPath().replace("/students/", "");
            String studentName = t.getRequestBody().toString();
            if (students.containsKey(studentId)) {
                students.put(studentId, studentName);
                return "Student updated successfully";
            } else {
                return "Student not found";
            }
        }

        private String handleDelete(HttpExchange t) {
            String studentId = t.getRequestURI().getPath().replace("/students/", "");
            if (students.containsKey(studentId)) {
                students.remove(studentId);
                return "Student deleted successfully";
            } else {
                return "Student not found";
            }
        }
    }
}