package com.example.quiz.addElements;

import com.example.quiz.HelloApplication;
import com.example.quiz.controllers.LkAdminController;
import com.example.quiz.controllers.LkPrepodController;
import com.example.quiz.controllers.LkStudentController;
import com.example.quiz.objects.Test;
import com.example.quiz.objects.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ResponseCreateTest {
    private static Test test;
    private static User user;

    public static Test getTest() {
        return test;
    }

    public static void setTest(Test test) {
        ResponseCreateTest.test = test;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        ResponseCreateTest.user = user;
    }
    public static void sendRequest(Label labelExeption){
        try {
            URL url = new URL("http://localhost:8000/user/createTest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody;

            requestBody = mapper.writeValueAsString(test);
            System.out.println(requestBody);

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(requestBody.getBytes(StandardCharsets.UTF_8));
            }
            if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                String[] message = response.toString().replaceAll("\\{|}","").split("=");
                labelExeption.setText("Ошибка " + connection.getResponseCode() + ": " + message[1]);
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Выводим ответ от сервера
                System.out.println("Выводим ответ от сервера");
                System.out.println(response.toString());
                String message = response.toString().replaceAll("\\{|}", "");
                String[] pairs = message.split("(, )");
                HashMap<String, String> parsedResponse = new HashMap<>();
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    parsedResponse.put("\"" + keyValue[0] + "\"", "\"" + keyValue[1] + "\"");
                }
                System.out.println(parsedResponse);
                if (parsedResponse.get("\"message\"").equals("\"success\"")) {
                    newWindow(labelExeption);
                } else {
                    labelExeption.setText(parsedResponse.get("\"message\"").replaceAll("\"", ""));
                }

                connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            labelExeption.setText("Connection not founded");
        }

    }

    private static void newWindow(Label labelExeption) throws IOException {
        Stage stage = (Stage) labelExeption.getScene().getWindow();
        stage.close();


    }
}
