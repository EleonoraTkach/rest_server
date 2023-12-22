package ru.quiz.server.entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_test", nullable = false)
    private Long idTest;
    @Column(name = "question", nullable = false)
    private String question;
    @Column(name = "type_question", nullable = false)
    private String typeQuestion;
    public Question() {
        this.id = id;
        this.idTest = idTest;
        this.question = question;
        this.typeQuestion = typeQuestion;
    }

    public Question(Long id, Long idTest, String question, String typeQuestion) {
        this.id = id;
        this.idTest = idTest;
        this.question = question;
        this.typeQuestion = typeQuestion;
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

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", idTest=" + idTest +
                ", question='" + question + '\'' +
                ", typeQuestion='" + typeQuestion + '\'' +
                '}';
    }
}