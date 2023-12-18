package com.example.quiz.objects;

import java.util.ArrayList;
import java.util.List;

public class Qwestion {
    private Long id;
    private Long idTest;
    private String qwestion;
    private String typeQwestion;
    private Integer quantityAnswer;
    List<Answer> answer = new ArrayList();

    public Qwestion(String qwestion, String typeQwestion, Integer quantityAnswer) {
        this.qwestion = qwestion;
        this.typeQwestion = typeQwestion;
        this.quantityAnswer = quantityAnswer;
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

    public Integer getQuantityAnswer() {
        return quantityAnswer;
    }

    public void setQuantityAnswer(Integer quantityAnswer) {
        this.quantityAnswer = quantityAnswer;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }
}
