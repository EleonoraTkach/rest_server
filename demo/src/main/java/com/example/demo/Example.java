package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.IOException;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Example {
    @FXML
    private VBox pane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private void clickOnContinueButton() throws IOException {
        // Создаем коллекцию vBox, которая будет содержать коллекции объектов для каждого вопроса
        ObservableList<VBox> vBox = FXCollections.observableArrayList();
        scrollPane.setPrefViewportHeight(200);
        scrollPane.setPrefViewportWidth(300);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        while (8 < pane.getChildren().size()) {
            System.out.println(pane.getChildren().size());
            pane.getChildren().remove(8);
        }
        for (int i = 1; i <= 10; i++) {
            VBox newVBox = addNewQuestion(i);
            newVBox.setFillWidth(true);
            vBox.addAll(newVBox);
        }
        System.out.println(pane.getPrefWidth());
        pane.setPrefHeight(USE_COMPUTED_SIZE);
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(400);
        line.setEndY(0);
// Добавляем коллекцию vBox в pane
        pane.getChildren().addAll(line);
        pane.getChildren().addAll(vBox);
        scrollPane.setFitToWidth(true);
        pane.setFillWidth(true);
        scrollPane.setFitToHeight(false);

    }

    private VBox addNewQuestion(Integer number) {
        ObservableList<Node> objects = FXCollections.observableArrayList();
        ObservableList<String> variants = FXCollections.observableArrayList("Выбор одного правильного ответа", "Выбор нескольких правильных ответов", "Вписать ответ");
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(400);
        line.setEndY(0);
        VBox content = new VBox();
        ComboBox typeQuestions = new ComboBox<>(variants);
        TextField text = new TextField();
        Button buttonNote = new Button("Заполнить ответы");
        buttonNote.setOnAction(event -> {
            System.out.println(number + " " + typeQuestions.getValue() + text.getText());
            if (typeQuestions.getValue() != null && !(text.getText() == null || text.getText().equals(""))) {
                Object value = typeQuestions.getValue();
                if (value.equals("Выбор одного правильного ответа")) {
                    while (5 < content.getChildren().size()) {
                        System.out.println(content.getChildren().size());
                        content.getChildren().remove(5);
                    }
                    ToggleGroup group = new ToggleGroup();
                    RadioButton rad1 = new RadioButton();
                    RadioButton rad2 = new RadioButton();
                    RadioButton rad3 = new RadioButton();
                    RadioButton rad4 = new RadioButton();
                    rad1.setToggleGroup(group);
                    rad2.setToggleGroup(group);
                    rad3.setToggleGroup(group);
                    rad4.setToggleGroup(group);

                    for (int i = 0; i < 4; i++) {
                        HBox hbox = null;
                        switch(i) {
                            case 0: hbox = new HBox(new TextField(), rad1); break;
                            case 1: hbox = new HBox(new TextField(), rad2); break;
                            case 2: hbox = new HBox(new TextField(), rad3); break;
                            case 3: hbox = new HBox(new TextField(), rad4); break;
                        }
                        hbox.setAlignment(Pos.CENTER);
                        hbox.setPadding(new Insets(5, 5, 5, 5));
                        content.getChildren().addAll(hbox);
                    }
                    content.getChildren().addAll(line);
                } else if (value.equals("Выбор нескольких правильных ответов")) {
                    while (5 < content.getChildren().size()) {
                        System.out.println(content.getChildren().size());
                        content.getChildren().remove(5);
                    }

                    for (int i = 0; i < 4; i++) {
                        HBox hbox = new HBox(new TextField(), new CheckBox());
                        hbox.setAlignment(Pos.CENTER);
                        hbox.setPadding(new Insets(5, 5, 5, 5));
                        content.getChildren().addAll(hbox);
                    }
                    content.getChildren().addAll(line);
                } else if (value.equals("Вписать ответ")) {

                    while (5 < content.getChildren().size()) {
                        System.out.println(content.getChildren().size());
                        content.getChildren().remove(5);
                    }
                    TextField textField = new TextField();
                    textField.setAlignment(Pos.CENTER);
                    textField.setPadding(new Insets(5, 5, 5, 5));
                    content.getChildren().addAll(textField);
                    content.getChildren().addAll(line);
                }
            }
        });
        Label label = new Label("Вопрос " + number + ": ");
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(20, 5, 5, 5));
        objects.addAll(label, text,new Label("Тип ответа:"), typeQuestions,buttonNote,line);
        content.setAlignment(Pos.CENTER);
        //content.setPadding(new Insets(10, 40, 10, 40));
        content.setSpacing(7.0);
        content.getChildren().addAll(objects);
        return content;
    }
}
