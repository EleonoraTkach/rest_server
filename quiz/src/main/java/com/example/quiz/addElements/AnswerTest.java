package com.example.quiz.addElements;

import com.example.quiz.objects.Test;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AnswerTest {
    private String message;
    private List<Test> tests;
    @JsonCreator
    public AnswerTest(
            // Добавляем аннотацию @JsonProperty к каждому параметру
            @JsonProperty("message") String message,
            @JsonProperty("tests") List<Test> tests
    ) {
        this.message = message;
        this.tests = tests;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    @Override
    public String toString() {
        return "AnswerTest{" +
                "message='" + message + '\'' +
                ", tests=" + tests +
                '}';
    }
}
