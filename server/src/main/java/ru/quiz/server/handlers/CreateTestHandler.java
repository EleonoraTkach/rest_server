package ru.quiz.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import ru.quiz.server.client.AnswerClient;
import ru.quiz.server.client.QuestionClient;
import ru.quiz.server.client.TestClient;
import ru.quiz.server.entities.Answer;
import ru.quiz.server.entities.Question;
import ru.quiz.server.entities.Test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class CreateTestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String requestMethod = exchange.getRequestMethod();
        String str = "";
        int rCode = 200;

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
                System.out.println(requestBody);
                TestClient test = mapper.readValue(requestBody.toString(), TestClient.class);
                Test dbTest = new Test(test.getId(),test.getTitle(),test.getTopic(), test.getQuantity(), test.getIdUser());
                System.out.println(dbTest);
                session.save(dbTest);
                for (int i = 0; i < test.getQuestions().size();i++) {
                    QuestionClient que = test.getQuestions().get(i);
                    Question dbQuestion = new Question(que.getId(),dbTest.getId(),que.getQuestion(),que.getTypeQuestion());
                    session.save(dbQuestion);
                    System.out.println(dbQuestion);
                    for (int j = 0; j < que.getAnswerClients().size(); j ++) {
                        AnswerClient ans = que.getAnswerClients().get(j);
                        Answer dbAnswer = new Answer(ans.getId(),dbQuestion.getId(), ans.getAnswer(), ans.getRightans(), ans.getKoefPoint());
                        session.save(dbAnswer);
                        System.out.println(dbAnswer);
                    }
                }
                transaction.commit();
                str = "success";
            } catch (ConstraintViolationException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                rCode = 400;
                str = "test with this id is exists";
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

        HashMap<String, String> response = new HashMap<>();

        response.put("message", str);
        byte[] bytes = response.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(rCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
