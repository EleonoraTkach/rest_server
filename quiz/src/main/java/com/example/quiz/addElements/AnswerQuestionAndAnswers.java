package com.example.quiz.addElements;

import com.example.quiz.objects.Question;
import com.example.quiz.objects.Test;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AnswerQuestionAndAnswers {
    private String message;
    private List<Question> question;
    @JsonCreator
    public AnswerQuestionAndAnswers(
            // Добавляем аннотацию @JsonProperty к каждому параметру
            @JsonProperty("message") String message,
            @JsonProperty("questions") List<Question> question
    ) {
        this.message = message;
        this.question = question;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Question> getQuestion() {
        return question;
    }

    public void setQuestion(List<Question> question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "AnswerQuestionAndAnswers{" +
                "message='" + message + '\'' +
                ", question=" + question +
                '}';
    }
}
