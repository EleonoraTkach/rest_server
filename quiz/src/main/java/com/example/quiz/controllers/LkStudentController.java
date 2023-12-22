package com.example.quiz.controllers;

import com.example.quiz.HelloApplication;
import com.example.quiz.addElements.AnswerResult;
import com.example.quiz.addElements.AnswerTest;
import com.example.quiz.objects.ResultAppoint;
import com.example.quiz.objects.Test;
import com.example.quiz.objects.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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

public class LkStudentController implements Initializable {
    @FXML
    private VBox vBoxTab3;
    @FXML
    private Label errorMessageTab3;
    @FXML
    private Label errorMessageTab2;
    @FXML
    private VBox vBoxTab2;
    private static User user;
    @FXML
    private Label unicNumber;
    @FXML
    private Button logOut;
    @FXML
    private TextField fio;
    @FXML
    private TextField login;
    @FXML
    private TextField type;
    @FXML
    private PasswordField newPasswordOne;
    @FXML
    private PasswordField newPasswordTwo;
    @FXML
    private Label oldLabel;
    @FXML
    private Label newLabel;
    @FXML
    private Button changeButton;
    @FXML
    private Label errorMessage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fio.setText(user.getFullName());
        login.setText(user.getEmail());
        fio.setText(user.getFullName());
        login.setText(user.getEmail());
        fio.setEditable(false);
        login.setEditable(false);
        type.setEditable(false);
        oldLabel.setVisible(false);
        newLabel.setVisible(false);
        newPasswordTwo.setVisible(false);
        newPasswordOne.setVisible(false);
        newPasswordTwo.setText(user.getPassword());
        newPasswordOne.setText(user.getPassword());
        Long num = user.getId() + 135;
        unicNumber.setText(" " + num);
        refreshTab2();
        refreshTab3();

    }
    @FXML
    private void clickOnChangeButton() {
        if (changeButton.getText().equals("Редактировать профиль")) {
            changeButton.setText("Сохранить изменения");
            oldLabel.setVisible(true);
            newLabel.setVisible(true);
            newPasswordTwo.setVisible(true);
            newPasswordOne.setVisible(true);
            login.setEditable(true);
        } else {
            System.out.println(newPasswordTwo.getText() + " " + newPasswordOne.getText());
            if (!newPasswordTwo.getText().equals(newPasswordOne.getText())) {
                errorMessage.setText("Пароли не совпадают");
            } else {
                if (login.getText() == null) {
                    errorMessage.setText("Поле с логином должно быть заполнено");
                } else {
                    try {
                        if (login.getText().equals(user.getEmail()) && !newPasswordOne.getText().equals(user.getPassword())) {
                            System.out.println(1);
                            String requestBody = "{\"id\": \"" + user.getId() + "\",\"password\": \"" + newPasswordOne.getText() + "\"}";
                            StringBuffer response = sendHTTP("http://localhost:8000/user/updatePassword", requestBody);
                            if (response != null) {
                                HashMap<String, String> parsedResponse = jsonObject(response);
                                if (parsedResponse.get("\"message\"").equals("\"success\"")) {
                                    user.setPassword(parsedResponse.get("\"password\"").replaceAll("\"", ""));
                                    theEnd();
                                } else {
                                    errorMessage.setText("Ошибка " + ": " + parsedResponse.get("\"message\"").replaceAll("\"", ""));
                                }
                            }
                        } else if (!login.getText().equals(user.getEmail())) {
                            System.out.println(2);
                            String requestBody;
                            if (newPasswordOne.getText().equals(user.getPassword())) {
                                requestBody = "{\"id\": \"" + user.getId() + "\",\"email\": \"" + login.getText() + "\"}";
                            } else {
                                requestBody = "{\"id\": \"" + user.getId() + "\",\"email\": \"" + login.getText() + "\",\"password\": \"" + newPasswordOne.getText() + "\"}";
                            }
                            StringBuffer response = sendHTTP("http://localhost:8000/user/updatePasswordAndEmail", requestBody);

                            if (response != null) {
                                HashMap<String, String> parsedResponse = jsonObject(response);
                                if (parsedResponse.get("\"message\"").equals("\"success\"")) {
                                    user.setEmail(parsedResponse.get("\"email\"").replaceAll("\"", ""));
                                    if (!(parsedResponse.get("\"password\"") == null)) {
                                        user.setPassword(parsedResponse.get("\"password\"").replaceAll("\"", ""));
                                    }
                                    theEnd();
                                } else {
                                    errorMessage.setText("Ошибка " + ": " + parsedResponse.get("\"message\"").replaceAll("\"", ""));
                                }

                            }
                        } else if (login.getText().equals(user.getEmail()) && newPasswordOne.getText().equals(user.getPassword())) {
                            System.out.println(3);
                            theEnd();
                        }
                    } catch (Exception e) {
                        errorMessage.setText("Не удалось установить соединение с сервером");
                    }
                }

            }
        }
    }
    @FXML
    private void clickOnLogOutButton() throws IOException {
        clickLogOut();
        System.out.println(user.toString());
        user = null;
    }
    private void theEnd() {
        changeButton.setText("Редактировать профиль");
        fio.setEditable(false);
        login.setEditable(false);
        type.setEditable(false);
        oldLabel.setVisible(false);
        newLabel.setVisible(false);
        newPasswordTwo.setVisible(false);
        newPasswordOne.setVisible(false);
        newPasswordTwo.setText(user.getPassword());
        newPasswordOne.setText(user.getPassword());
        errorMessage.setText("Данные успешно изменены");
    }

    private void clickLogOut() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("log-in.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 450);
            Stage stage = (Stage) logOut.getScene().getWindow();;
            stage.setTitle("Вход в тестовую систему");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User userNew) {
        user = userNew;
    }
    private HashMap<String, String> jsonObject(StringBuffer response) {
        String message = response.toString().replaceAll("\\{|}", "");
        String[] pairs = message.split("(, )");
        HashMap<String, String> parsedResponse = new HashMap<>();
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            parsedResponse.put("\"" + keyValue[0] + "\"", "\"" + keyValue[1] + "\"");
        }
        System.out.println(parsedResponse);
        return parsedResponse;
    }
    private StringBuffer sendHTTP(String urlCon, String requestBody) {
        try {
            URL url = new URL(urlCon);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(requestBody.getBytes(StandardCharsets.UTF_8));
            }
            StringBuffer response = new StringBuffer();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

            } else {
                // Получаем ответ от сервера
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                connection.disconnect();
            }
            return response;



        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setText("Не удалось установить соединение с сервером");
            return null;
        }
    }

    public HBox addNodeForTest(Test test) {
        HBox container = new HBox();
        VBox containerForLabel = new VBox(new Label("Тест: " + test.getTitle()),
                new Label("Тема: " + test.getTopic()),new Label("Количество вопросов: " + test.getQuantity()));
        Button buttonView = new Button("Пройти тест");
        buttonView.setOnAction(event -> {
            try {
                ViewTestController.setTest(test);
                ViewTestController.setUser(user);

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("viewTest.fxml"));
                Stage stage = new Stage();
                stage.setOnCloseRequest(eventClose -> {
                    vBoxTab2.getChildren().clear();
                    refreshTab2();
                    vBoxTab3.getChildren().clear();
                    refreshTab3();
                });
                stage.setTitle("Тест");
                Scene scene = new Scene(fxmlLoader.load(), 500, 450);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        VBox containerForButton = new VBox(buttonView);
        containerForLabel.setAlignment(Pos.CENTER);
        containerForButton.setAlignment(Pos.CENTER);
        containerForLabel.setPadding(new Insets(5, 5, 5, 5));
        containerForButton.setPadding(new Insets(5, 5, 5, 5));
        container.getChildren().addAll(containerForLabel,containerForButton);
        return container;
    }
    public void clickOnRefreshButtonTab2(ActionEvent actionEvent) {
        vBoxTab2.getChildren().clear();
        refreshTab2();
    }

    public void refreshTab2() {
        errorMessageTab2.setText("");
        try {
            URL url = new URL("http://localhost:8000/user/selectTestsStudents");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            String requestBody = "{\"id\": \"" + user.getId() + "\"}";
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
                errorMessageTab2.setText("Ошибка " + connection.getResponseCode() + ": " + message[1].split("\""));
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
                AnswerTest answer = mapper.readValue(message, AnswerTest.class);
                System.out.println(answer);
                for (int i = 0; i < answer.getTests().size();i++) {
                    Test test = answer.getTests().get(i);
                    vBoxTab2.getChildren().addAll(addNodeForTest(test));
                }

                if (vBoxTab2.getChildren().size() == 0) {
                    errorMessageTab2.setText("Нет назначенных тестов");
                }
                connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorMessageTab2.setText("Connection not founded");
        }
    }

    public HBox addNodeForResult(ResultAppoint resultAppoint) {
        HBox container = new HBox();
        VBox containerForLabel = new VBox(new Label("Тест: " + resultAppoint.getTitleTest())
                ,new Label("Количество баллов: " + resultAppoint.getResult()));
        containerForLabel.setAlignment(Pos.CENTER);
        containerForLabel.setPadding(new Insets(5, 5, 5, 5));
        container.getChildren().addAll(containerForLabel);
        return container;
    }

    public void refreshTab3() {
        errorMessageTab3.setText("");
        try {
            URL url = new URL("http://localhost:8000/user/selectResultTestYour");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            String requestBody = "{\"id\": \"" + user.getId() + "\"}";
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
                errorMessageTab3.setText("Ошибка " + connection.getResponseCode() + ": " + message[1].split("\""));
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
                AnswerResult answerResult = mapper.readValue(message, AnswerResult.class);
                System.out.println(answerResult);
                System.out.println(vBoxTab3.getChildren().size());
                for (int i = 0; i < answerResult.getResults().size();i++) {
                    ResultAppoint resultAppoint = answerResult.getResults().get(i);
                    vBoxTab3.getChildren().addAll(addNodeForResult(resultAppoint));
                }
                if (vBoxTab3.getChildren().size() == 0) {
                    errorMessageTab3.setText("Нет результатов тестов студента");
                }
                connection.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorMessageTab3.setText("Connection not founded");
        }
    }

    public void clickOnButtonRefreshTab3(ActionEvent actionEvent) {
        vBoxTab3.getChildren().clear();
        refreshTab3();
    }
}
