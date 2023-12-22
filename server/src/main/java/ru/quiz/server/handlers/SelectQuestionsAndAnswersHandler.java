package ru.quiz.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.quiz.server.client.AnswerClient;
import ru.quiz.server.client.QuestionClient;
import ru.quiz.server.entities.Answer;
import ru.quiz.server.entities.Question;
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

public class SelectQuestionsAndAnswersHandler implements HttpHandler {
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
                Test test = mapper.readValue(requestBody.toString(), Test.class);
                String hql = "select q from Question q where q.idTest = " + test.getId();
                Query<Question> query = session.createQuery(hql , Question.class);
                List<Question> questions = query.getResultList();
                List<QuestionClient> questionsForResponse = new ArrayList<>();
                for (int i = 0; i < questions.size(); i++) {
                    System.out.println(questions.get(i));
                    Question que = questions.get(i);
                    QuestionClient questionClient = new QuestionClient(que.getId(), que.getIdTest(),que.getQuestion(),que.getTypeQuestion(),null);
                    String hqlNew = "select a from Answer a where a.idQuestion = " + que.getId();
                    Query<Answer> queryNew = session.createQuery(hqlNew , Answer.class);
                    List<Answer> ans = queryNew.getResultList();
                    List<AnswerClient> answers = new ArrayList<>();
                    for (int k = 0; k < ans.size();k++) {
                        AnswerClient answerClient = new AnswerClient(ans.get(k).getId(),ans.get(k).getIdQuestion(),
                                ans.get(k).getAnswer(),ans.get(k).getRight(),ans.get(k).getKoefPoint());
                        answers.add(answerClient);
                    }
                    questionClient.setAnswerClients(answers);
                    questionsForResponse.add(questionClient);
                }
                String json = mapper.writeValueAsString (questionsForResponse);
                String jsonSend = json.replaceAll("answerClients","answers");
                System.out.println (jsonSend);
                response.put("\"questions\"", jsonSend);
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
