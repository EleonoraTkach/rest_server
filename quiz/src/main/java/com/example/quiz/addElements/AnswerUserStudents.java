package com.example.quiz.addElements;

import com.example.quiz.objects.Test;
import com.example.quiz.objects.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AnswerUserStudents {
    private String message;
    private List<User> users;
    @JsonCreator
    public AnswerUserStudents(
            // Добавляем аннотацию @JsonProperty к каждому параметру
            @JsonProperty("message") String message,
            @JsonProperty("users") List<User> users
    ) {
        this.message = message;
        this.users = users;
    }

    @Override
    public String toString() {
        return "AnswerUserStudents{" +
                "message='" + message + '\'' +
                ", users=" + users +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
