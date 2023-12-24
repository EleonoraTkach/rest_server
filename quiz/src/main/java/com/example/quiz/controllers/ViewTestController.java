package com.example.quiz.controllers;

import com.example.quiz.HelloApplication;
import com.example.quiz.addElements.AnswerQuestionAndAnswers;
import com.example.quiz.addElements.AnswerTest;
import com.example.quiz.addElements.Role;
import com.example.quiz.addElements.TypeQuestion;
import com.example.quiz.objects.Answer;
import com.example.quiz.objects.Question;
import com.example.quiz.objects.Test;
import com.example.quiz.objects.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewTestController implements Initializable {
    @FXML
    private Label title;
    @FXML
    private Label errorMessage;
    @FXML
    private VBox vStart;
    @FXML
    private AnchorPane pane;
    @FXML
    private Label topic;
    @FXML
    private Label quantity;
    private static Test test;
    private static User user;
    private Integer currentIndex = 0;
    private Double result = 0.0;
    private static List <VBox> objects;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        httpqwestions();
        List<Question> questions = test.getQuestions();
        objects = FXCollections.observableArrayList();
        for (int i = 0; i < questions.size(); i++) {
            VBox content = new VBox();
            if (user.getRole() == Role.STUDENT.getValue()) {
                content.getChildren().addAll(new Label("Bопрос " + (i + 1) + ": "), addQuestionStudent(questions.get(i),i,questions.size()));
            } else if (user.getRole() == Role.TEACHER.getValue()) {
                content.getChildren().addAll(new Label("Bопрос " + (i + 1) + ": "), addQuestionPrepod(questions.get(i),i,questions.size()));
            }
            objects.add(content);
        }
        title.setText(test.getTitle());
        topic.setText(test.getTopic());
        quantity.setText(test.getQuantity().toString());
    }
    private VBox addQuestionStudent(Question question, Integer n, Integer size) {
        VBox container = new VBox();
        List<Answer> answers = question.getAnswers();
        container.getChildren().addAll(new Label(question.getQuestion()));
        if (question.getTypeQuestion() == TypeQuestion.ONEASWER.getValue()) {
            ToggleGroup group = new ToggleGroup();
            RadioButton rad1 = new RadioButton();
            RadioButton rad2 = new RadioButton();
            RadioButton rad3 = new RadioButton();
            RadioButton rad4 = new RadioButton();
            rad1.setToggleGroup(group);
            rad2.setToggleGroup(group);
            rad3.setToggleGroup(group);
            rad4.setToggleGroup(group);
            for (int i = 0; i < answers.size(); i++) {
                HBox hbox = null;
                Answer answer = answers.get(i);
                switch(i) {
                    case 0: hbox = new HBox(rad1, new Label(answer.getAnswer())); break;
                    case 1: hbox = new HBox(rad2, new Label(answer.getAnswer())); break;
                    case 2: hbox = new HBox(rad3, new Label(answer.getAnswer())); break;
                    case 3: hbox = new HBox(rad4, new Label(answer.getAnswer())); break;
                }
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(5, 5, 5, 5));
                container.getChildren().addAll(hbox);
            }

        } else if (question.getTypeQuestion() == TypeQuestion.MANYANSWER.getValue()) {
            for (int i = 0; i < 4; i++) {
                Answer answer = answers.get(i);
                HBox hbox = new HBox(new CheckBox(), new Label(answer.getAnswer()));
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(5, 5, 5, 5));
                container.getChildren().addAll(hbox);
            }
        } else {
            TextField textField = new TextField();
            textField.setAlignment(Pos.CENTER);
            textField.setPadding(new Insets(5, 5, 5, 5));
            container.getChildren().addAll(textField);
        }
        Button buttonNext = new Button("Next question");
        buttonNext.setOnAction(event -> {
            click(1);
        });
        Button buttonBefore = new Button("Before question");
        buttonBefore.setOnAction(event -> {
            click(-1);
        });
        Button buttonEnd = new Button("End Test");
        buttonEnd.setOnAction(event -> {
            result = 0.0;
            countResult();
            result = 100 * result/test.getQuantity();
            Double scale = Math.pow(10, 2);
            result = Math.ceil(result * scale) / scale;
            System.out.println(result + " до запроса");
            if (httpSaveResult()) {
                VBox containerNew = new VBox();
                Label label = new Label("Ваш результат: " + result);
                containerNew.getChildren().add(label);
                System.out.println(objects.size() + " " + currentIndex);
                objects.add(containerNew);
                System.out.println(objects.size() + " " + currentIndex);
                click(1);
            }
            System.out.println(result);

        });
        if (size == 1) {
            container.getChildren().addAll(buttonEnd);
        }
        else if (n == 0) {
            container.getChildren().addAll(buttonNext);
        } else if (n == size - 1) {
            container.getChildren().addAll(buttonBefore);
            container.getChildren().addAll(buttonEnd);
        } else {
            container.getChildren().addAll(buttonNext);
            container.getChildren().addAll(buttonBefore);
        }

        return container;
    }

    private void countResult() {
        List <Question> questions = test.getQuestions();
        for (int i = 0;i < objects.size();i++) {
           VBox nowVBox = (VBox) objects.get(i).getChildren().get(1);
           Question questionNow = questions.get(i);
           List<Answer> answers = questions.get(i).getAnswers();
           if (questionNow.getTypeQuestion() == TypeQuestion.ONEASWER.getValue()) {
               for (int j = 0;j < answers.size();j++) {
                   HBox hBox = (HBox) nowVBox.getChildren().get(j + 1);
                   RadioButton rad = (RadioButton) hBox.getChildren().get(0);
                   if (rad.isSelected() == answers.get(j).getRightans()) {
                       result += answers.get(j).getKoefPoint();
                   }
               }
           } else if (questionNow.getTypeQuestion() == TypeQuestion.MANYANSWER.getValue()) {
               Double count = 0.0;
               Integer kolvo = 0;
               for (int j = 0;j < answers.size();j++) {
                   HBox hBox = (HBox) nowVBox.getChildren().get(j + 1);
                   CheckBox checkBox = (CheckBox) hBox.getChildren().get(0);
                   if (checkBox.isSelected() == answers.get(j).getRightans()) {
                       count += answers.get(j).getKoefPoint();
                   } else if (!answers.get(j).getRightans()) {
                       kolvo++;
                   }
               }
               if (count != 0.0 && kolvo == 0) {
                   result += count;
               }
           } else {
               TextField textField = (TextField) nowVBox.getChildren().get(1);
               if (textField.getText().equals(answers.get(0).getAnswer())) {
                   result += answers.get(0).getKoefPoint();
               }
           }

        }
    }

    private VBox addQuestionPrepod(Question question, Integer n, Integer size) {
        VBox container = new VBox();
        List<Answer> answers = question.getAnswers();
        container.getChildren().addAll(new Label(question.getQuestion()));
        if (question.getTypeQuestion() == TypeQuestion.ONEASWER.getValue()) {
            ToggleGroup group = new ToggleGroup();
            RadioButton rad1 = new RadioButton();
            RadioButton rad2 = new RadioButton();
            RadioButton rad3 = new RadioButton();
            RadioButton rad4 = new RadioButton();
            rad1.setToggleGroup(group);
            rad2.setToggleGroup(group);
            rad3.setToggleGroup(group);
            rad4.setToggleGroup(group);
            for (int i = 0; i < answers.size(); i++) {
                HBox hbox = null;
                Answer answer = answers.get(i);
                switch(i) {
                    case 0: hbox = new HBox(rad1, new Label(answer.getAnswer()));rad1.setSelected(answer.getRightans()); rad1.setDisable(true);break;
                    case 1: hbox = new HBox(rad2, new Label(answer.getAnswer()));rad2.setSelected(answer.getRightans()); rad2.setDisable(true);break;
                    case 2: hbox = new HBox(rad3, new Label(answer.getAnswer()));rad3.setSelected(answer.getRightans()); rad3.setDisable(true);break;
                    case 3: hbox = new HBox(rad4, new Label(answer.getAnswer()));rad4.setSelected(answer.getRightans()); rad4.setDisable(true);break;
                }
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(5, 5, 5, 5));
                container.getChildren().addAll(hbox);
            }

        } else if (question.getTypeQuestion() == TypeQuestion.MANYANSWER.getValue()) {
            for (int i = 0; i < 4; i++) {
                Answer answer = answers.get(i);
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(answer.getRightans());
                checkBox.setDisable(true);
                HBox hbox = new HBox(checkBox, new Label(answer.getAnswer()));
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(5, 5, 5, 5));
                container.getChildren().addAll(hbox);
            }
        } else {
            TextField textField = new TextField();
            textField.setText(answers.get(0).getAnswer());
            textField.setEditable(false);
            textField.setAlignment(Pos.CENTER);
            textField.setPadding(new Insets(5, 5, 5, 5));
            container.getChildren().addAll(textField);
        }
        Button buttonNext = new Button("Next question");
        buttonNext.setOnAction(event -> {
            click(1);
        });
        Button buttonBefore = new Button("Before question");
        buttonBefore.setOnAction(event -> {
            click(-1);
        });
        Button buttonEnd = new Button("End Test");
        buttonEnd.setOnAction(event -> {
            Stage stage = (Stage) buttonEnd.getScene().getWindow();
            stage.close();
        });
        if (size == 1) {
            container.getChildren().addAll(buttonEnd);
        }
        else if (n == 0) {
            container.getChildren().addAll(buttonNext);
        } else if (n == size - 1) {
            container.getChildren().addAll(buttonBefore);
            container.getChildren().addAll(buttonEnd);
        } else {
            container.getChildren().addAll(buttonNext);
            container.getChildren().addAll(buttonBefore);
        }

        return container;
    }

    private void httpqwestions() {
        errorMessage.setText("");
        try {
            URL url = new URL("http://localhost:8000/user/selectQuestions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            String requestBody = "{\"id\": \"" + test.getId() + "\"}";
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
                errorMessage.setText("Ошибка " + connection.getResponseCode() + ": " + message[1].split("\""));
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
                AnswerQuestionAndAnswers answer = mapper.readValue(message, AnswerQuestionAndAnswers.class);
                System.out.println(answer);
                test.setQuestions(answer.getQuestion());
                System.out.println(test);
                connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setText("Connection not founded");
        }
    }

    private Boolean httpSaveResult(){
        System.out.println(result + " в запроса");
        errorMessage.setText("");
        try {

            URL url = new URL("http://localhost:8000/user/updateResult");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            String requestBody = "{\"idTest\": \"" + test.getId() + "\", \"idStudent\": \"" + user.getId() + "\",\"result: \"" + result + "\"}";
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
                errorMessage.setText("Ошибка " + connection.getResponseCode() + ": " + message[1].split("\""));
                return false;
            } else {
                connection.disconnect();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setText("Connection not founded");
            return false;
        }
    }

    private void click(Integer i) {
        pane.getChildren().remove(objects.get(currentIndex));
        pane.getChildren().add(objects.get(currentIndex + i));
        currentIndex += i;
    }
    @FXML
    private void clickOnStartButton(){
        System.out.println(currentIndex + " " +  objects.get(currentIndex));
        pane.getChildren().remove(vStart);
        pane.getChildren().add(objects.get(currentIndex));
    };

    public static Test getTest() {
        return test;
    }

    public static void setTest(Test test) {
        ViewTestController.test = test;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        ViewTestController.user = user;
    }
}
