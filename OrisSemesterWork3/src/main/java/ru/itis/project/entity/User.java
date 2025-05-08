package ru.itis.project.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String hashedPassword;

    private Integer skillPoints;

    private Float avgRating;

    private String description;
}
