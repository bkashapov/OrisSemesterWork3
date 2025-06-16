package ru.itis.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Lecture {

    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne
    private Skill skill;

    private String title;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
