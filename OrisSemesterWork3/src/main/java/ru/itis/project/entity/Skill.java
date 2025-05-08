package ru.itis.project.entity;

import javax.persistence.*;

@Entity
public class Skill {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private Float rating;

    private String description;
}
