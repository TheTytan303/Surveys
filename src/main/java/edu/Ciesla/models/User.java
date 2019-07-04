package edu.Ciesla.models;

import javax.persistence.*;

@Entity
@Table(name = "Users", uniqueConstraints=@UniqueConstraint(columnNames = {"user_id"}))
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "user_id", unique=true, nullable = false)
    private int user_id;

    @Column(name = "username")
    private String username;

    public User(){}
    public User(String name){
        this.username=name;
    }

    public int getUser_id() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
}
