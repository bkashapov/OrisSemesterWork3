package ru.itis.project.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String hashedPassword;

    private Integer skillPoints;

    private Double avgRating;

    private String description;
}
