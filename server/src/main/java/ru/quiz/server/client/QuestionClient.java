package ru.quiz.server.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.quiz.server.entities.Answer;

import java.util.ArrayList;
import java.util.List;

public class QuestionClient {
    private Long id;
    private Long idTest;
    private String question;
    private String typeQuestion;
    List<AnswerClient> answers = new ArrayList();

    @JsonCreator
    public QuestionClient(
            // Добавляем аннотацию @JsonProperty к каждому параметру
            @JsonProperty("id") Long id,
            @JsonProperty("idTest") Long idTest,
            @JsonProperty("question") String question,
            @JsonProperty("typeQuestion") String typeQuestion,
            @JsonProperty("answers") List<AnswerClient> answers

    ) {
        this.id = id;
        this.idTest = idTest;
        this.question = question;
        this.typeQuestion = typeQuestion;
        this.answers = answers;

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

    public List<AnswerClient> getAnswerClients() {
        return answers;
    }

    public void setAnswerClients(List<AnswerClient> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", idTest=" + idTest +
                ", question='" + question + '\'' +
                ", typeQuestion='" + typeQuestion + '\'' +
                ", answers=" + answers +
                '}';
    }
}
