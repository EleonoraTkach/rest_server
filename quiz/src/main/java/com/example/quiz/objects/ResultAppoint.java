package com.example.quiz.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultAppoint {
    private String fioStudent;
    private String titleTest;
    private Double result;
    @JsonCreator
    public ResultAppoint(
            // Добавляем аннотацию @JsonProperty к каждому параметру
            @JsonProperty("fioStudent") String fioStudent,
            @JsonProperty("titleTest") String titleTest,
            @JsonProperty("result") Double result

    ) {
        this.fioStudent = fioStudent;
        this.titleTest = titleTest;
        this.result = result;
    }

    public String getFioStudent() {
        return fioStudent;
    }

    public void setFioStudent(String fioStudent) {
        this.fioStudent = fioStudent;
    }

    public String getTitleTest() {
        return titleTest;
    }

    public void setTitleTest(String titleTest) {
        this.titleTest = titleTest;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

}
