package com.example.quiz.objects;

public class Answer {
    private Long id;
    private Long idQuestion;
    private String answer;
    private Boolean rightans;
    private Double koefPoint;

    public Answer(String answer, Boolean rightans, Double koefPoint) {
        this.answer = answer;
        this.rightans = rightans;
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
}
