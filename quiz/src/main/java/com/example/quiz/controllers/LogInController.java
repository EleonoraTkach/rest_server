package com.example.quiz.controllers;

import com.example.quiz.HelloApplication;
import com.example.quiz.objects.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

public class LogInController {
    @FXML
    private TextField loginLogIn;
    @FXML
    private TextField passwordLogIn;
    @FXML
    private Label logInExeption;
    @FXML
    private Hyperlink logUpLink;
    @FXML
    private void clickOnLogInButton() {
        logInExeption.setText("");
        String strLogin = loginLogIn.getText();
        String strPassword = passwordLogIn.getText();
        try {
            URL url = new URL("http://localhost:8000/user/auth");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Устанавливаем метод запроса (GET, POST и т.д.)
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Включаем возможность отправки данных в тело запроса
            connection.setDoOutput(true);

            // Создаем тело запроса
            String requestBody = "{\"email\": \"" + strLogin + "\", \"password\": \"" + strPassword + "\"}";

            // Получаем поток для записи данных в тело запроса
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(requestBody.getBytes(StandardCharsets.UTF_8));
            }

            // Получаем ответ от сервера
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),  "UTF-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Выводим ответ от сервера
            System.out.println(response.toString());
            ObjectMapper mapper = new ObjectMapper();
            String message = response.toString().replaceAll("\\{|}","");
            String[] pairs = message.split("(, )");

// Создаем Map для хранения пар ключ-значение
            HashMap<String, String> parsedResponse = new HashMap<>();

// Разбиваем каждую пару ключ-значение по знаку равно и добавляем в Map
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                parsedResponse.put("\"" + keyValue[0] + "\"", "\"" + keyValue[1]  + "\"");
            }
            System.out.println(parsedResponse);
            if (parsedResponse.get("\"message\"").equals("\"success\"")) {
                User user = new User(Long.parseLong(parsedResponse.get("\"id\"").replaceAll("\"","")), parsedResponse.get("\"fullName\""), parsedResponse.get("\"email\""), parsedResponse.get("\"role\""));
                System.out.println(user.toString());

            } else {
                logInExeption.setText(parsedResponse.get("\"message\"").replaceAll("\"",""));
            }

            //System.out.println(user.toString());

            // Закрываем соединение
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }


















//        Database dataBase = new Database();
//        String sql = "SELECT * FROM users WHERE login_user = \'" + strLogin + "\'";
//        if (dataBase.qwery(sql).equals(strLogin + " " + strPassword)) {
//            System.out.println("Вход в систему");
//        } else {
//            logInExeptiion.setText("Неправильный логин или пароль, попробуйте ещё раз.");
//        };


    }
    @FXML
    private void clickOnLogUpLink(ActionEvent event) throws IOException{
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("log-up.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 450);
            Stage stage = (Stage) logUpLink.getScene().getWindow();;
            stage.setTitle("Регистрация");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}