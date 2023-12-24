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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectUsersHandler implements HttpHandler {
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

            String idTest = requestBody.toString().replaceAll("[^\\d+]","");
            System.out.println(idTest);
            try {
                String hql = "select u from User u where u.role = 3 and u.is_validated = true";

                String sql = "SELECT users.* FROM users INNER JOIN appoint ON appoint.id_student = users.id and appoint.result IS NULL and appoint.id_test = \'" + idTest + "\' WHERE users.role_user = 3 and users.is_validated = true";
                Query queryAll = session.createQuery(hql, User.class);
                List<User> usersAll = queryAll.getResultList();
                System.out.println(usersAll);

                Query<User> query = session.createNativeQuery(sql , User.class);
                List<User> usersWrong = query.getResultList();
                System.out.println(usersWrong);

                for (int i = 0; i < usersWrong.size(); i++) {
                    User userWrong = usersWrong.get(i);
                    for (int j = 0;j < usersAll.size();j++) {
                        if (userWrong.getId() == usersAll.get(j).getId()) {
                            usersAll.remove(userWrong);
                        }
                    }
                }
                String json = mapper.writeValueAsString (usersAll);
                System.out.println (json);
                response.put("\"users\"", json);
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
