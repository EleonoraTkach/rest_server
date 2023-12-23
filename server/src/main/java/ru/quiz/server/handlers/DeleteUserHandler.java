package ru.quiz.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.quiz.server.entities.Appoint;
import ru.quiz.server.entities.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class DeleteUserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
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
                String hql = "select a from Appoint a where a.idStudent = " + user.getId() + " or a.idPrepod = " + user.getId();
                Query<Appoint> query = session.createQuery(hql , Appoint.class);
                List<Appoint> list = query.getResultList();
                if (!(list == null || list.isEmpty())){
                    for (int i = 0; i < list.size();i++) {
                        session.delete(list.get(i));
                    }
                }

                User userNew = session.get(User.class, user.getId());
                userNew.setIs_validated(true);
                session.delete(userNew);
                str = "success";
                transaction.commit();
            } catch (Exception e){
                if (transaction != null) {
                    transaction.rollback();
                }
                rCode = 400;
                str = "Не удалось отклонить регистрацию пользователя";
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



        response.put("message", str);

        System.out.println(response);
        System.out.println(rCode);
        byte[] bytes = response.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(rCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
