package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
public class CreateTestController implements Initializable {
    private static User user = new User(5L,"bhjwbr","jvnwjr","cnel","ncjwkrfnvj");
    @FXML
    private VBox pane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField nameTest;
    @FXML
    private TextField topicTest;
    @FXML
    private Spinner<Integer> spinner = new Spinner<>(1, 70, 1);
    @FXML
    private Label errorMessage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 70, 1));
    }
    @FXML
    private void clickOnContinueButton() throws IOException {

        if (!(nameTest.getText() == null || (nameTest.getText().equals(""))) && !(topicTest.getText() == null || (topicTest.getText().equals(""))) && spinner.getValue() != null) {
            try {
                System.out.println(user.toString());
                if (Integer.parseInt(String.valueOf(spinner.getValue())) > 0) {
                    Integer n = Integer.parseInt(String.valueOf(spinner.getValue()));
                    ObservableList<VBox> vBox = FXCollections.observableArrayList();
                    scrollPane.setPrefViewportHeight(200);
                    scrollPane.setPrefViewportWidth(300);
                    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                    while (8 < pane.getChildren().size()) {
                        System.out.println(pane.getChildren().size());
                        pane.getChildren().remove(8);
                    }
                    for (int i = 1; i <= n; i++) {
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
                    pane.getChildren().addAll(line);
                    pane.getChildren().addAll(vBox);
                    scrollPane.setFitToWidth(true);
                    scrollPane.setFitToHeight(false);
                    Button buttonSave = new Button("Создать тест");
                    buttonSave.setOnAction(event -> {
                        Test test = null;
                        if (!(nameTest.getText() == null || (nameTest.getText().equals(""))) && !(topicTest.getText() == null || (topicTest.getText().equals(""))) && spinner.getValue() != null) {
                            test = new Test(nameTest.getText(),topicTest.getText(), Integer.parseInt(String.valueOf(spinner.getValue())), user.getId());
                        } else {
                            errorMessage.setText("Все поля должны быть заполнены");
                        }
                        Boolean pullAll = true;
                        System.out.println(vBox.size());
                        for (int l = 0; l < vBox.size(); l++) {
                            System.out.println(l + " прогонка");
                            if (saveTest(vBox.get(l)) == null) {
                                pullAll  = false;
                            }
                            test.getQwestions().add(saveTest(vBox.get(l)));
                        }
                        if (pullAll) {
                            buttonSave.setText("Сохранено");
                        }
                        System.out.println(test);
                        responseCreateTest.setTest(test);
                        responseCreateTest.setUser(user);
                    });
                    pane.getChildren().addAll(buttonSave);
                } else {
                    errorMessage.setText("Количество вопросов должно быть целым положительным числом");
                }
            } catch (NumberFormatException e) {
                errorMessage.setText("Количество вопросов должно быть целым положительным числом");
            }

        } else {
            errorMessage.setText("Все поля должны быть заполнены");
        }

    }
    private Qwestion saveTest(VBox content) {
        String str = "";
        Qwestion newQwestion = null;
        Label labelError = (Label) content.getChildren().get(5);
        labelError.setTextFill(Color.RED);
        TextField textField = (TextField) content.getChildren().get(1);
        ComboBox typeQuestions = (ComboBox) content.getChildren().get(3);
        System.out.println(textField.getText() + " " + typeQuestions.getValue());
        if (typeQuestions.getValue() != null && !(textField.getText() == null || textField.getText().equals(""))) {
            newQwestion = new Qwestion(textField.getText(), (String) typeQuestions.getValue());
            labelError.setText("");
            Object value = typeQuestions.getValue();
            Boolean isSelectOneRadiobutton = false;
            if (content.getChildren().get(6).getClass() != Line.class) {
                if (value.equals("Выбор одного правильного ответа")) {
                    for (int i = 6; i < 10; i++) {
                        HBox hbox = (HBox) content.getChildren().get(i);
                        TextField textFieldAnswer = (TextField) hbox.getChildren().get(0);
                        RadioButton radioButton = (RadioButton) hbox.getChildren().get(1);
                        Double bal = 0.0;
                        Answer answer;
                        System.out.println(textFieldAnswer.getText());
                        if (!(textFieldAnswer.getText() == null || textFieldAnswer.getText().equals(""))) {
                            if (radioButton.isSelected()) {
                                bal = 1.0;
                                isSelectOneRadiobutton = true;
                            }
                            answer = new Answer(textFieldAnswer.getText(), radioButton.isSelected(), bal);
                            newQwestion.getAnswers().add(answer);
                        } else {
                            str = "Все поля должны быть заполнены";
                            labelError.setText(str);
                            newQwestion = null;
                            return newQwestion;
                        }

                    }
                    if (!isSelectOneRadiobutton) {
                        str = "Необходимо выбрать один правильный ответ";
                        labelError.setText(str);
                        newQwestion = null;
                    }

                    return newQwestion;
                } else if (value.equals("Выбор нескольких правильных ответов")) {
                    Integer kolvo = 0;
                    for (int i = 6; i < 10; i++) {
                        HBox hbox = (HBox) content.getChildren().get(i);
                        TextField textFieldAnswer = (TextField) hbox.getChildren().get(0);
                        CheckBox checkBox = (CheckBox) hbox.getChildren().get(1);
                        Answer answer;
                        if (!(textFieldAnswer.getText() == null || textFieldAnswer.getText().equals(""))) {
                            if (checkBox.isSelected()) {
                                kolvo += 1;
                            }
                            answer = new Answer(textFieldAnswer.getText(), checkBox.isSelected(), 0.0);
                            newQwestion.getAnswers().add(answer);
                        } else {
                            str = "Все поля должны быть заполнены";
                            labelError.setText(str);
                            newQwestion = null;
                            return newQwestion;
                        }


                    }
                    if (kolvo < 2) {
                        str = "Необходимо выбрать хотя бы два правильных ответа";
                        labelError.setText(str);
                        newQwestion = null;
                        return newQwestion;
                    } else {
                        for (int j = 0; j < 4; j++) {
                            if (newQwestion.getAnswers().get(j).getRight()) {
                                newQwestion.getAnswers().get(j).setKoefPoint(kolvo/4.0);
                            }
                        }
                        return newQwestion;
                    }

                } else if (value.equals("Вписать ответ")) {
                    TextField textFieldAnswer = (TextField) content.getChildren().get(6);
                    if (!(textFieldAnswer.getText() == null || textFieldAnswer.getText().equals(""))) {
                        Answer answer = new Answer(textFieldAnswer.getText(), true, 1.0);
                        newQwestion.getAnswers().add(answer);
                        return newQwestion;
                    } else {
                        str = "Все поля должны быть заполнены";
                        labelError.setText(str);
                        newQwestion = null;
                        return newQwestion;
                    }
                }
            } else {
                labelError.setText("Необходимо нажать кнопку продолжить для заполнения ответов");
                newQwestion = null;
                return newQwestion;
            }

        } else {
            str = "Все поля должны быть заполнены";
            labelError.setText(str);
            newQwestion = null;
            return newQwestion;
        }
        return newQwestion;
    }
    private VBox addNewQuestion(Integer number) {
        ObservableList<Node> objects = FXCollections.observableArrayList();
        ObservableList<String> variants = FXCollections.observableArrayList("Выбор одного правильного ответа", "Выбор нескольких правильных ответов", "Вписать ответ");
        Label labelError = new Label();
        labelError.setAlignment(Pos.CENTER);
        labelError.setPadding(new Insets(5, 5, 5, 5));
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

            if (typeQuestions.getValue() != null && !(text.getText() == null || text.getText().equals(""))) {
                labelError.setText("");
                Object value = typeQuestions.getValue();
                if (value.equals("Выбор одного правильного ответа")) {
                    while (6 < content.getChildren().size()) {
                        System.out.println(content.getChildren().size());
                        content.getChildren().remove(6);
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
                    while (6 < content.getChildren().size()) {
                        System.out.println(content.getChildren().size());
                        content.getChildren().remove(6);
                    }

                    for (int i = 0; i < 4; i++) {
                        HBox hbox = new HBox(new TextField(), new CheckBox());
                        hbox.setAlignment(Pos.CENTER);
                        hbox.setPadding(new Insets(5, 5, 5, 5));
                        content.getChildren().addAll(hbox);
                    }
                    content.getChildren().addAll(line);
                } else if (value.equals("Вписать ответ")) {

                    while (6 < content.getChildren().size()) {
                        System.out.println(content.getChildren().size());
                        content.getChildren().remove(6);
                    }
                    TextField textField = new TextField();
                    textField.setAlignment(Pos.CENTER);
                    textField.setPadding(new Insets(5, 5, 5, 5));
                    content.getChildren().addAll(textField);
                    content.getChildren().addAll(line);
                }
            } else {
                labelError.setText("Заполните суть вопроса и его тип, чтобы перейти к следующему шагу");
                while (7 < content.getChildren().size()) {
                    System.out.println(content.getChildren().size());
                    content.getChildren().remove(6);
                }

            }
        });
        Label label = new Label("Вопрос " + number + ": ");
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(20, 5, 5, 5));


        objects.addAll(label, text,new Label("Тип ответа:"), typeQuestions,buttonNote,labelError,line);
        content.setAlignment(Pos.CENTER);
        //content.setPadding(new Insets(10, 40, 10, 40));
        content.setSpacing(7.0);
        content.getChildren().addAll(objects);
        return content;
    }
}
