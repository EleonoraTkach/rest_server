package com.example.quiz.controllers;

import com.example.quiz.HelloApplication;
import com.example.quiz.objects.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LogUpController implements Initializable {
    @FXML
    private TextField fio;
    @FXML
    private TextField email;
    @FXML
    private TextField passwordOne;
    @FXML
    private TextField passwordTwo;
    @FXML
    private ComboBox role;
    @FXML
    private Button sendButton;
    @FXML
    private Label errorMessage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> variants = FXCollections.observableArrayList("Cтудент", "Преподаватель", "Администратор");
        role.setItems(variants);
    }
    public void clickOnSendButton() {
        String fioUser = fio.getText();
        String emailUser = email.getText();
        String password1User = passwordOne.getText();
        String password2User = passwordTwo.getText();
        Boolean is_validated = false;
        if ((fioUser != null && !fioUser.equals("")) && (emailUser != null && !emailUser.equals(""))  && (password1User != null && !password1User.equals(""))  && (password2User != null && !password2User.equals(""))  && role.getValue() != null) {
            System.out.println(password2User);
            if (password1User.equals(password2User)) {
                try {
                    URL url = new URL("http://localhost:8000/user/register");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setDoOutput(true);
                    String requestBody = "{\"fullName\" : \"" + fioUser + "\", \"email\": \"" + emailUser + "\", \"password\": \"" + password1User + "\", \"role\": \"" + role.getValue() + "\", \"is_validated\": \"" + is_validated + "\"}";
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
                        errorMessage.setText("Ошибка " + connection.getResponseCode() + ": " + message[1]);
                    } else {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),  "UTF-8"));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        System.out.println(response.toString());
                        System.out.println(response.toString().replaceAll("\\{|}",""));
                        String[] message = response.toString().replaceAll("\\{|}","").split("=");
                        errorMessage.setText("успех");
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("log-in.fxml"));
                            Scene scene = new Scene(fxmlLoader.load(), 500, 450);
                            Stage stage = (Stage) sendButton.getScene().getWindow();;
                            stage.setTitle("Вход в тестовую систему");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        connection.disconnect();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessage.setText("Нет соединения с сервером");
                }
            } else {
                errorMessage.setText("Введенные пароли не совпадают");
            }
        } else {
            errorMessage.setText("Все поля должны быть заполнены");
        }
    }

}
