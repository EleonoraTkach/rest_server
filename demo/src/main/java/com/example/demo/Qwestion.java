package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Qwestion {
    private Long id;
    private Long idTest;
    private String qwestion;
    private String typeQwestion;
    List<Answer> answers = new ArrayList();

    public Qwestion(String qwestion, String typeQwestion) {
        this.qwestion = qwestion;
        this.typeQwestion = typeQwestion;
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

    public String getQwestion() {
        return qwestion;
    }

    public void setQwestion(String qwestion) {
        this.qwestion = qwestion;
    }

    public String getTypeQwestion() {
        return typeQwestion;
    }

    public void setTypeQwestion(String typeQwestion) {
        this.typeQwestion = typeQwestion;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Qwestion{" +
                "id=" + id +
                ", idTest=" + idTest +
                ", qwestion='" + qwestion + '\'' +
                ", typeQwestion='" + typeQwestion + '\'' +
                ", answers=" + answers +
                '}';
    }
}
