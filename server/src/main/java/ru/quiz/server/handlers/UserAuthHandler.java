package ru.quiz.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
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

public class UserAuthHandler implements HttpHandler {
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
                System.out.println(user.toString());
                /*CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<User> criteria = builder.createQuery(User.class);
                Root<User> root = criteria.from(User.class);
                Predicate login = builder.equal(root.get("email"), user.getEmail());
                Predicate password = builder.equal(root.get("password"), user.getPassword());
                criteria.select(root).where(builder.and(login, password));
                Query<User> qwery = session.createQuery(criteria);
                List<User> list = qwery.getResultList();*/
                String hql = "select u from User u where u.email = \'" + user.getEmail() + "\' and u.password = \'" + user.getPassword() + "\'";
                System.out.println(hql);
                Query queryAll = session.createQuery(hql, User.class);
                List<User> list = queryAll.getResultList();
                System.out.println(list.toString());

                if (list == null || list.isEmpty()) {
                    rCode = 400;
                    if (user.getEmail() == null || user.getPassword() == null){
                        str = "all fields must be filled in";
                    } else {
                        str = "the username or password was entered incorrectly";
                    }

                } else {
                    if (list.get(0).getIs_validated()){
                        str = "success";
                        System.out.println(list.get(0).getId() + "");
                        response.put("id", list.get(0).getId() + "");
                        response.put("fullName", list.get(0).getFullName());
                        response.put("role", list.get(0).getRole());
                        response.put("email", list.get(0).getEmail());
                        response.put("password", list.get(0).getPassword());
                    } else {
                        str = "the administrator did not confirm the account";
                    }
                }
                transaction.commit();
            } catch (Exception e){
                if (transaction != null) {
                    transaction.rollback();
                }
                rCode = 400;
                str = e.getMessage();
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
