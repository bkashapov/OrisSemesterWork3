package ru.itis.project.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String email;

    private String hashedPassword;

    private Integer skillPoints;

    private Double avgRating;

    private String description;
}
