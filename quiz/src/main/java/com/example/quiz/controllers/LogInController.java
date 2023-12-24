package com.example.quiz.controllers;

import com.example.quiz.HelloApplication;
import com.example.quiz.addElements.Role;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private Button logInButton;
    public String securePassword(String password) {
        byte[] bytesOfPwd = password.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytesOfPwd);
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    @FXML
    private void clickOnLogInButton() {
        System.out.println(securePassword("sidor"));
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
            String requestBody = "{\"email\": \"" + strLogin + "\", \"password\": \"" + securePassword(strPassword) + "\"}";
            // Получаем поток для записи данных в тело запроса
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
                logInExeption.setText("Ошибка " + connection.getResponseCode() + ": " + message[1]);
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Выводим ответ от сервера
                System.out.println(response.toString());
                String message = response.toString().replaceAll("\\{|}", "");
                String[] pairs = message.split("(, )");

// Создаем Map для хранения пар ключ-значение
                HashMap<String, String> parsedResponse = new HashMap<>();

// Разбиваем каждую пару ключ-значение по знаку равно и добавляем в Map
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    parsedResponse.put("\"" + keyValue[0] + "\"", "\"" + keyValue[1] + "\"");
                }
                System.out.println(parsedResponse);
                if (parsedResponse.get("\"message\"").equals("\"success\"")) {
                    String path  = "";
                    User user = new User(Long.parseLong(parsedResponse.get("\"id\"").replaceAll("\"", "")), parsedResponse.get("\"fullName\"").replaceAll("\"", ""), parsedResponse.get("\"email\"").replaceAll("\"", ""), Integer.parseInt(parsedResponse.get("\"role\"").replaceAll("\"", "")), parsedResponse.get("\"password\"").replaceAll("\"", ""));
                    if (user.getRole() == Role.TEACHER.getValue()) {
                        path = "lkPrepod.fxml";
                        LkPrepodController.setUser(user);
                    } else if (user.getRole() == Role.ADMIN.getValue()) {
                        path = "lkAdmin.fxml";
                        LkAdminController.setUser(user);
                    } else if (user.getRole() == Role.STUDENT.getValue()){
                        path = "lkStudent.fxml";
                        LkStudentController.setUser(user);
                    } else {
                        logInExeption.setText("Пользователя с таким типом не существует");
                    }
                    clickLogIn(path);
                    System.out.println(user.toString());

                } else {
                    logInExeption.setText(parsedResponse.get("\"message\"").replaceAll("\"", ""));
                }

                connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            logInExeption.setText("Connection not founded");
        }
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
    private void clickLogIn(String path) throws IOException{
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(path));
            Scene scene = new Scene(fxmlLoader.load(), 650, 450);
            Stage stage = (Stage) logInButton.getScene().getWindow();;
            stage.setTitle("Личный кабинет");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}