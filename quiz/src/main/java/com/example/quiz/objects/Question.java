package com.example.quiz.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private Long id;
    private Long idTest;
    private String question;
    private Integer typeQuestion;
    List<Answer> answers = new ArrayList();

    @JsonCreator
    public Question(
            @JsonProperty("question") String question,
            @JsonProperty("typeQuestion") Integer typeQuestion
    ) {
        this.question = question;
        this.typeQuestion = typeQuestion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTest() {
        return idTest;
    }

    public void setIdTest(Long idTest) {
        this.idTest = idTest;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getTypeQuestion() {
        return typeQuestion;
    }

    public void setTypeQuestion(Integer typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", idTest=" + idTest +
                ", question='" + question + '\'' +
                ", typeQuestion='" + typeQuestion + '\'' +
                '}';
    }
}
