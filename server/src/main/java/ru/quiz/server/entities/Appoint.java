package ru.quiz.server.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.quiz.server.client.QuestionClient;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "appoint")
public class Appoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_test", nullable = false)
    private Long idTest;
    @Column(name = "id_student", nullable = false)
    private Long idStudent;
    @Column(name = "id_prepod", nullable = false)
    private Long idPrepod;
    @Column(name = "result")
    private Double result;

    @JsonCreator
    public Appoint(
            // Добавляем аннотацию @JsonProperty к каждому параметру
            @JsonProperty("idTest") Long idTest,
            @JsonProperty("idStudent") Long idStudent,
            @JsonProperty("idPrepod") Long idPrepod

    ) {
        this.idTest = idTest;
        this.idStudent = idStudent;
        this.idPrepod = idPrepod;
    }


    public Appoint() {
    }

    @Override
    public String toString() {
        return "Appoint{" +
                "id=" + id +
                ", idTest=" + idTest +
                ", idStudent=" + idStudent +
                ", idPrepod=" + idPrepod +
                ", result=" + result +
                '}';
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

    public Long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Long idStudent) {
        this.idStudent = idStudent;
    }

    public Long getIdPrepod() {
        return idPrepod;
    }

    public void setIdPrepod(Long idPrepod) {
        this.idPrepod = idPrepod;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}
