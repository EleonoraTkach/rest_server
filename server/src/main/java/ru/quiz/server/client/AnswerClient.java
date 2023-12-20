package ru.quiz.server.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnswerClient {
    private Long id;
    private Long idQuestion;
    private String answer;
    private Boolean rightans;
    private Double koefPoint;

    @JsonCreator
    public AnswerClient(
            // Добавляем аннотацию @JsonProperty к каждому параметру
            @JsonProperty("id") Long id,
            @JsonProperty("idQuestion") Long idQuestion,
            @JsonProperty("answer") String answer,
            @JsonProperty("rightans") Boolean rightans,
            @JsonProperty("koefPoint") Double koefPoint

    ) {
        this.id = id;
        this.idQuestion = idQuestion;
        this.answer = answer;
        this.rightans = rightans;
        this.koefPoint = koefPoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getRightans() {
        return rightans;
    }

    public void setRightans(Boolean rightans) {
        this.rightans = rightans;
    }

    public Double getKoefPoint() {
        return koefPoint;
    }

    public void setKoefPoint(Double koefPoint) {
        this.koefPoint = koefPoint;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", idQuestion=" + idQuestion +
                ", answer='" + answer + '\'' +
                ", rightans=" + rightans +
                ", koefPoint=" + koefPoint +
                '}';
    }
}
