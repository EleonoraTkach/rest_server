package ru.quiz.server.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.quiz.server.client.QuestionClient;

import java.util.ArrayList;
import java.util.List;

public class TestClient {
    private Long id;
    private String title;
    private String topic;
    private Integer quantity;
    private Long idUser;
    List<QuestionClient> questions = new ArrayList();

    // Добавляем аннотацию @JsonCreator к конструктору
    @JsonCreator
    public TestClient(
            // Добавляем аннотацию @JsonProperty к каждому параметру
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("topic") String topic,
            @JsonProperty("quantity") Integer quantity,
            @JsonProperty("idUser") Long idUser,
            @JsonProperty("questions") List<QuestionClient> questions
    ) {
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.quantity = quantity;
        this.idUser = idUser;
        this.questions = questions;
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

    public List<QuestionClient> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionClient> questions) {
        this.questions = questions;
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
}
