package ru.quiz.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import ru.quiz.server.entities.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class UserUpdatePasswordAndEmailHandler implements HttpHandler {
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

            try {
                User user = mapper.readValue(requestBody.toString(), User.class);
                User userNew = session.get(User.class, user.getId());
                /*CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<User> criteria = builder.createQuery(User.class);
                Root<User> root = criteria.from(User.class);
                Predicate login = builder.equal(root.get("email"), user.getEmail());
                criteria.select(root).where(login);
                Query<User> qwery = session.createQuery(criteria);
                List<User> list = qwery.getResultList();

                if (list == null || list.isEmpty()) {*/
                    userNew.setEmail(user.getEmail());
                    response.put("email", user.getEmail());
                    str = "success";
                    if (user.getPassword() != null) {
                        userNew.setPassword(user.getPassword());
                        response.put("password", user.getPassword());
                    }
                /*} else {
                    rCode = 400;
                    str = "user with this login is exists";
                }*/

                session.update(userNew);
                System.out.println(userNew.getPassword() + "");
                transaction.commit();
            } catch (ConstraintViolationException e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                rCode = 400;
                str = "user with this login is exists";
            }  catch (Exception e){
                if (transaction != null) {
                    transaction.rollback();
                }
                rCode = 400;
                str = "user with this login is exists";
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
