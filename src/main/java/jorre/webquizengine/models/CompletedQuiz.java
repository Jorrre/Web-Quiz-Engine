package jorre.webquizengine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CompletedQuiz {

    @Id
    @GeneratedValue
    @JsonIgnore
    private long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Quiz quiz;

    private LocalDateTime completedAt;

    @ManyToOne
    @JsonIgnore
    private User completedBy;

    public CompletedQuiz() {
    }

    public CompletedQuiz(Quiz quiz, LocalDateTime completedAt, User completedBy) {
        this.quiz = quiz;
        this.completedAt = completedAt;
        this.completedBy = completedBy;
    }

    @Override
    public String toString() {
        return "CompletedQuiz{" +
                "id=" + id +
                ", quiz=" + quiz +
                ", solvedAt=" + completedAt +
                ", solvedBy=" + completedBy +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    @JsonProperty(value = "quizId")
    public long getQuizId() {
        return quiz.getId();
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime solvedAt) {
        this.completedAt = solvedAt;
    }

    public User getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(User solvedBy) {
        this.completedBy = solvedBy;
    }

}
