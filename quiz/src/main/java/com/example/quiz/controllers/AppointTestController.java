package com.example.quiz.controllers;

import com.example.quiz.addElements.AnswerTest;
import com.example.quiz.addElements.AnswerUserStudents;
import com.example.quiz.objects.Test;
import com.example.quiz.objects.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppointTestController implements Initializable {
    private static User user;
    private static Test test;
    @FXML
    private VBox vBoxAppoint;
    @FXML
    private Label empty;
    private List<User> students;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        students = new ArrayList<>();
        selectStudent();
    }
    private void addNodeForAppoint(User newUser){
        HBox container = new HBox();
        VBox vBox = new VBox();
        Label errorLab = new Label();
        Label fio = new Label(newUser.getFullName());
        Long num = user.getId() + 135;
        Label unicNumber = new Label(num.toString());
        Button button = new Button("Назначить");
        vBox.getChildren().addAll(unicNumber, fio, errorLab);
        container.getChildren().addAll(vBox,button);
        button.setOnAction(event -> {
            try {
                URL url = new URL("http://localhost:8000/user/selectAppoint");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);
                String requestBody = "{\"idStudent\": \"" + newUser.getId() + "\", \"idTest\": \"" + test.getId() + "\"," +
                        "\"idPrepod\": \"" + user.getId() + "\"}";
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
                    errorLab.setText("Ошибка " + connection.getResponseCode() + ": " + message[1]);
                } else {
                    vBoxAppoint.getChildren().remove(container);
                    System.out.println(vBoxAppoint.getChildren().size());
                    if (vBoxAppoint.getChildren().size() == 2) {
                        empty.setText("Никому нельзя назначить тест");
                    }
                    connection.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
                errorLab.setText("Connection not founded");
            }
        });
        vBoxAppoint.getChildren().add(container);


    }

    public void selectStudent() {
        Label errorLabel = new Label();
        vBoxAppoint.getChildren().add(errorLabel);
        try {
            URL url = new URL("http://localhost:8000/user/selectUsers");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            String requestBody = "{\"idTest\": \"" + test.getId() + "\"}";
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
                errorLabel.setText("Ошибка " + connection.getResponseCode() + ": " + message[1].split("\""));
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response.toString());
                ObjectMapper mapper = new ObjectMapper();
                String message = response.toString().replaceAll("=",":");
                System.out.println(message);
                AnswerUserStudents answer = mapper.readValue(message, AnswerUserStudents.class);
                System.out.println(answer);
                for (int i = 0; i < answer.getUsers().size();i++) {
                    User userNew = answer.getUsers().get(i);
                    students.add(userNew);
                    System.out.println(userNew);
                    addNodeForAppoint(userNew);
                }
                if (answer.getUsers().size() == 0) {
                    System.out.println(vBoxAppoint.getChildren().size());
                    if (vBoxAppoint.getChildren().size() == 2) {
                        empty.setText("Никому нельзя назначить тест");
                    }
                }
                connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Connection not founded");
        }
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        AppointTestController.user = user;
    }

    public static Test getTest() {
        return test;
    }

    public static void setTest(Test test) {
        AppointTestController.test = test;
    }

    public VBox getvBoxAppoint() {
        return vBoxAppoint;
    }

    public void setvBoxAppoint(VBox vBoxAppoint) {
        this.vBoxAppoint = vBoxAppoint;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }
}
