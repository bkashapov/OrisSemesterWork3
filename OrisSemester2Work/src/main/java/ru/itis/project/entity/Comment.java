package ru.itis.project.entity;

import jakarta.persistence.*;

@Entity
public class Comment {

    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne
    private User author;

    @ManyToOne
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    private String text;

    private int likes;


}
