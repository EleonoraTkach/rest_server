package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private Long id;
    private String nameTest;
    private String topicTest;
    private Integer quantityQuestions;
    private Long idUser;
    List<Qwestion> qwestions = new ArrayList();

    public Test(String nameTest, String topicTest, Integer quantityQuestions, Long idUser) {
        this.nameTest = nameTest;
        this.topicTest = topicTest;
        this.quantityQuestions = quantityQuestions;
        this.idUser = idUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNameTest() {
        return nameTest;
    }

    public void setNameTest(String nameTest) {
        this.nameTest = nameTest;
    }

    public String getTopicTest() {
        return topicTest;
    }

    public void setTopicTest(String topicTest) {
        this.topicTest = topicTest;
    }

    public Integer getQuantityQuestions() {
        return quantityQuestions;
    }

    public void setQuantityQuestions(Integer quantityQuestions) {
        this.quantityQuestions = quantityQuestions;
    }

    public List<Qwestion> getQwestions() {
        return qwestions;
    }

    public void setQwestions(List<Qwestion> qwestions) {
        this.qwestions = qwestions;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", nameTest='" + nameTest + '\'' +
                ", topicTest='" + topicTest + '\'' +
                ", quantityQuestions=" + quantityQuestions +
                ", idUser=" + idUser +
                ", qwestions=" + qwestions +
                '}';
    }
}
