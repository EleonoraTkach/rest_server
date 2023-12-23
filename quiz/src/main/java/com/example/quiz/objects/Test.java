package com.example.quiz.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private Long id;
    private String title;
    private String topic;
    private Integer quantity;
    private Long idUser;
    List<Question> questions = new ArrayList();

    /*public Test(String title, String topic, Integer quantity, Long idUser) {
        this.title = title;
        this.topic = topic;
        this.quantity = quantity;
        this.idUser = idUser;
    }*/
    @JsonCreator
    public Test(
            @JsonProperty("title") String title,
            @JsonProperty("topic") String topic,
            @JsonProperty("quantity") Integer quantity,
            @JsonProperty("idUser") Long idUser
    ) {
        this.title = title;
        this.topic = topic;
        this.quantity = quantity;
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", topic='" + topic + '\'' +
                ", quantity=" + quantity +
                ", idUser=" + idUser +
                ", questions=" + questions +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
