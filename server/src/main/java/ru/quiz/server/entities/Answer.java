package ru.quiz.server.entities;
import javax.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_question", nullable = false)
    private Long idQuestion;
    @Column(name = "answer", nullable = false)
    private String answer;
    @Column(name = "rightans", nullable = false)
    private Boolean rightans;
    @Column(name = "koef", nullable = false)
    private Double koefPoint;
    public Answer() {
        this.id = id;
        this.idQuestion = idQuestion;
        this.answer = answer;
        this.rightans = rightans;
        this.koefPoint = koefPoint;
    }

    public Answer(Long id, Long idQuestion, String answer, Boolean rightans, Double koefPoint) {
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

    public Boolean getRight() {
        return rightans;
    }

    public void setRight(Boolean rightans) {
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
