package edu.Ciesla.models;

import javax.persistence.*;

@Entity
@Table(name = "Surveys", uniqueConstraints=@UniqueConstraint(columnNames = {"survey_id"}))
public class Survey {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JoinColumn(name = "survey_id", unique=true, nullable = false)
    private int survey_id;

    @Column(name = "survey_title")
    private String survey_title;

    @Column(name = "question")
    private String question;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "user_id")
    private User user;



    public Survey(){}
    public Survey(User author, String title, String question)throws NullPointerException{
        if(author==null){
            throw new NullPointerException("Author does not exist");
        }
        this.user = author;
        if(question==null){
            throw new NullPointerException("Question is empty. (it is required)");
        }
        this.question = question;
        if(title==null){
            throw new NullPointerException("Title is empty. (it is required)");
        }
        this.survey_title=title;
    }

    public int getSurvey_id() {
        return survey_id;
    }
    public String getSurvey_title() {
        return survey_title;
    }
    public String getQuestion() {
        return question;
    }
    public User getUser_id() {
        return user;
    }

    public void setSurvey_id(int survey_id) {
        this.survey_id = survey_id;
    }
    public void setSurvey_title(String survey_title) {
        this.survey_title = survey_title;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setUser_id(User user_id) {
        this.user = user_id;
    }
}
