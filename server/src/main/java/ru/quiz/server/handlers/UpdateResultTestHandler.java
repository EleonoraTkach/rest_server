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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class UpdateResultTestHandler implements HttpHandler {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            System.out.println(requestBody);
            try {
                String[] newPar = requestBody.toString().split(",");
                String sql = "SELECT * FROM appoint WHERE id_test = " + Long.parseLong(newPar[0].replaceAll("[^\\d+]",""))
                        + " and id_student = " + Long.parseLong(newPar[1].replaceAll("[^\\d+]","")) +
                        " and appoint.result IS NULL";
                Query<Appoint> query = session.createNativeQuery(sql , Appoint.class);
                List<Appoint> list = query.getResultList();
                if (list.isEmpty() || list == null) {
                    System.out.println(list);
                    rCode = 400;
                    str = "\"can't appoint\"";
                } else {
                    Appoint lastApp = list.get(0);
                    System.out.println(lastApp);
                    Appoint appointNew = session.get(Appoint.class, lastApp.getId());
                    System.out.println(newPar[2]);
                    appointNew.setResult(Double.parseDouble(newPar[2].replaceAll("[^\\d+.\\d+]","")));

                    session.update(appointNew);
                    System.out.println(appointNew);
                    transaction.commit();
                    str = "\"success\"";
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
