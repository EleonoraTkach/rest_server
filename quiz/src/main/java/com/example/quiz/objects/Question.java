package com.example.quiz.objects;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private Long id;
    private Long idTest;
    private String question;
    private String typeQuestion;
    List<Answer> answers = new ArrayList();

    public Question(String question, String typeQuestion) {
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

    public String getTypeQuestion() {
        return typeQuestion;
    }

    public void setTypeQuestion(String typeQuestion) {
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
