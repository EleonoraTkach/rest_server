package com.example.quiz.addElements;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddCreateQwestion {
    private Label label;
    private TextField textField;

    public AddCreateQwestion(String strLabel, String strTextField) {
        this.label = new Label(strLabel);
        this.textField = new TextField(strLabel);
    }
}
