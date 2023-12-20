package com.example.demo;

public class Answer {
    private Long id;
    private Long idQwestion;
    private String nameAnswer;
    private Boolean isRight;
    private Double koefPoint;

    public Answer(String nameAnswer, Boolean isRight, Double koefPoint) {
        this.nameAnswer = nameAnswer;
        this.isRight = isRight;
        this.koefPoint = koefPoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdQwestion() {
        return idQwestion;
    }

    public void setIdQwestion(Long idQwestion) {
        this.idQwestion = idQwestion;
    }

    public String getNameAnswer() {
        return nameAnswer;
    }

    public void setNameAnswer(String nameAnswer) {
        this.nameAnswer = nameAnswer;
    }

    public Boolean getRight() {
        return isRight;
    }

    public void setRight(Boolean right) {
        isRight = right;
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
                ", idQwestion=" + idQwestion +
                ", nameAnswer='" + nameAnswer + '\'' +
                ", isRight=" + isRight +
                ", koefPoint=" + koefPoint +
                '}';
    }
}
