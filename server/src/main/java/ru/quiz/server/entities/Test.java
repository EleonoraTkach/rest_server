package ru.quiz.server.entities;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title_test", nullable = false)
    private String title;
    @Column(name = "topic_test", nullable = false)
    private String topic;
    @Column(name = "quantity_quenstion", nullable = false)
    private Integer quantity;

    @Column(name = "id_user", nullable = false)
    private Long idUser;

    public Test(Long id, String title, String topic, Integer quantity, Long idUser) {
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.quantity = quantity;
        this.idUser = idUser;
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

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", topic='" + topic + '\'' +
                ", quantity=" + quantity +
                ", idUser=" + idUser +
                '}';
    }
}
