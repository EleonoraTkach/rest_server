module com.example.quiz {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.example.quiz to javafx.fxml;
    exports com.example.quiz;
    exports com.example.quiz.controllers;
    opens com.example.quiz.controllers to javafx.fxml;
    exports com.example.quiz.objects;
    opens com.example.quiz.objects to javafx.fxml;
}