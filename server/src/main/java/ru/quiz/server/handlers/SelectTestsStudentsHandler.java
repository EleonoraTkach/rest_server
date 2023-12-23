package ru.quiz.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.quiz.server.entities.Test;
import ru.quiz.server.entities.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class SelectTestsStudentsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String requestMethod = exchange.getRequestMethod();
        String str = "";
        int rCode = 200;
        HashMap<String, String> response = new HashMap<>();

        if (requestMethod.equalsIgnoreCase("POST")) {
            ObjectMapper mapper = new ObjectMapper();
            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            System.out.println(requestBody);
            try {
                User user = mapper.readValue(requestBody.toString(), User.class);
                String sql = "SELECT test.* FROM appoint INNER JOIN users ON appoint.id_student = users.id and users.id = "
                        + user.getId() + " INNER JOIN test ON appoint.id_test = test.id WHERE result IS NULL";
                System.out.println(sql);
                Query<Test> query = session.createNativeQuery(sql , Test.class);
                List<Test> list = query.getResultList();
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i));
                }
                String json = mapper.writeValueAsString (list);
                System.out.println (json);
                response.put("\"tests\"", json);
                str = "\"success\"";

            } catch (Exception e){
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                rCode = 400;
                str = "all fields must be filled in";
            } finally {
                if (session != null) {
                    session.close();
                    sessionFactory.close();
                }

            }

        } else {
            session.close();
            sessionFactory.close();
            rCode = 400;
            str = "method of requrest is wrong";
        }

        response.put("\"message\"", str);
        byte[] bytes = response.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(rCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();

    }
}
