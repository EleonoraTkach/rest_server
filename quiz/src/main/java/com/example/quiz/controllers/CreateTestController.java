package com.example.quiz.controllers;

import com.example.quiz.addElements.AddCreateQwestion;
import com.example.quiz.objects.Test;
import com.example.quiz.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class CreateTestController {
    private static User user;
    @FXML
    private TextField nameTest;
    @FXML
    private TextField topicTset;
    @FXML
    private TextField quantityQwestions;
    @FXML
    private Label errorMessage;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane pane;

    public void clickOnContinueButton(ActionEvent actionEvent) {
        if ((nameTest.getText() == null || topicTset.getText() == null || quantityQwestions.getText() == null) || ((nameTest.getText().equals("") || topicTset.getText().equals("") || quantityQwestions.getText().equals("")))) {
            errorMessage.setText("Все поля должны быть заполнены");
        } else {
            try {
                System.out.println(user.toString());
                if (Integer.parseInt(quantityQwestions.getText()) > 0) {
                    Test test = new Test(nameTest.getText(), topicTset.getText(), Integer.parseInt(quantityQwestions.getText()), user.getId());
                    System.out.println(test);
                    ObservableList<Node> objects = FXCollections.observableArrayList();
                    scrollPane.setFitToWidth(true);
                    scrollPane.setFitToHeight(true);
                    scrollPane.setMinViewportHeight(200);
                    scrollPane.setPrefViewportWidth(300);
                    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                    VBox content = new VBox();
                    for (int i = 1; i <= Integer.parseInt(quantityQwestions.getText()); i++) {

                        objects.addAll(new Label("Вопрос:"), new TextField(),new Label("Тип ответа:"), new TextField());


                        content.getChildren().addAll(objects);



                        AnchorPane.setBottomAnchor(content, 0.0);
                    }
                    pane.getChildren().addAll(content);

// Добавление нескольких меток в коллекцию

                } else {
                    errorMessage.setText("Количество вопросов должно быть целым положительным числом");
                }
            } catch (NumberFormatException e) {
                errorMessage.setText("Количество вопросов должно быть целым положительным числом");
            }

        }
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        CreateTestController.user = user;
    }
}
