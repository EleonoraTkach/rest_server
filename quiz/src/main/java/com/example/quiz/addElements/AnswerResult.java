package com.example.quiz.addElements;

import com.example.quiz.objects.ResultAppoint;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AnswerResult {
    private String message;
    private List<ResultAppoint> appoints;
    @JsonCreator
    public AnswerResult(
            // Добавляем аннотацию @JsonProperty к каждому параметру
            @JsonProperty("message") String message,
            @JsonProperty("appoints") List<ResultAppoint> appoints

    ) {
        this.message = message;
        this.appoints = appoints;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultAppoint> getResults() {
        return appoints;
    }

    public void setAppoints(List<ResultAppoint> appoints) {
        this.appoints = appoints;
    }
}
