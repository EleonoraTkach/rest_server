package ru.quiz.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.quiz.server.client.AnswerResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectResultYourTestHandler implements HttpHandler {
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
            String idStudent = requestBody.toString().replaceAll("[^\\d+]","");
            System.out.println(idStudent);
            try {
                String sql = "SELECT users.full_name, test.title_test, appoint.result \n" +
                        "FROM appoint\n" +
                        "INNER JOIN users ON appoint.id_student = users.id\n" +
                        "INNER JOIN test ON appoint.id_test = test.id\n" +
                        "WHERE appoint.id_student = " + idStudent + " and result IS NOT NULL";
                Query query = session.createNativeQuery(sql);
                List<Object []> results = query.getResultList();

                List<AnswerResult> resultList = new ArrayList<>();
                for (Object [] obj : results) {
                    AnswerResult answerResult = new AnswerResult();
                    answerResult.setFioStudent ((String) obj[0]);
                    answerResult.setTitleTest ((String) obj[1]);
                    answerResult.setResult (Double.valueOf((Float) obj[2]));
                    resultList.add(answerResult);
                    System.out.println(answerResult);
                }


                str = "\"success\"";
                String json = mapper.writeValueAsString (resultList);
                response.put("\"appoints\"", json);

            } catch (Exception e){
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                rCode = 400;
                str = "\"can't view result\"";
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
