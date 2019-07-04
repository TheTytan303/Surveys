package edu.Ciesla.models;

import javax.persistence.*;

@Entity
@Table(name = "Answers", uniqueConstraints=@UniqueConstraint(columnNames = {"answer_id"}))
public class Answer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "answer_id", unique=true, nullable = false)
    private int answer_id;

    @ManyToOne
    @JoinColumn(name = "survey", referencedColumnName = "survey_id")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "user_id")
    private User user;

    @Column(name = "rating")
    private int rating;

    public Answer(User author, Survey s, int r) throws IndexOutOfBoundsException{
        this.user = author;
        this.rating =r;
        if(r< 0||r >5){
            throw new IndexOutOfBoundsException("Wrong mark input. (shall be between 0 and 5)");
        }
        this.survey=s;
    }
    public Answer(){};

    public int getAnswer_id() {
        return answer_id;
    }
    public Survey getSurvey_id() {
        return survey;
    }
    public User getUser_id() {
        return user;
    }
    public int getRating() {
        return rating;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }
    public void setSurvey_id(Survey survey_id) {
        this.survey = survey_id;
    }
    public void setUser_id(User user_id) {
        this.user = user_id;
    }
    public void setRating(int rating)throws IndexOutOfBoundsException {
        if(rating< 0||rating >5){
            throw new IndexOutOfBoundsException("Wrong mark input. (shall be between 0 and 5)");
        }
        this.rating = rating;
    }
}
