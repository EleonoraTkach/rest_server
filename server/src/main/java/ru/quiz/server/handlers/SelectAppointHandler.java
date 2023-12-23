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
import ru.quiz.server.entities.Test;
import ru.quiz.server.entities.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class SelectAppointHandler implements HttpHandler {
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
            System.out.println(requestBody.toString());
            try {
                Appoint appoint = mapper.readValue(requestBody.toString(), Appoint.class);
                String hql = "select a from Appoint a where a.idStudent = " + appoint.getIdStudent() + " and a.idTest = " + + appoint.getIdTest() + " and a.result IS NULL";
                Query<Appoint> query = session.createQuery(hql , Appoint.class);
                List<Appoint> list = query.getResultList();
                if (list == null || list.isEmpty()){
                    session.save(appoint);
                    transaction.commit();
                    System.out.println(appoint);
                    str = "\"success\"";
                } else {
                    rCode = 400;
                    str = "\"this test was appointed for this student\"";
                }

            } catch (Exception e){
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                rCode = 400;
                str = "\"can't appoint\"";
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
            str = "\"method of requrest is wrong\"";
        }

        response.put("\"message\"", str);
        byte[] bytes = response.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(rCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
