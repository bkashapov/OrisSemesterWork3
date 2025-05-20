package ru.itis.project.entity;

import jakarta.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;
}
