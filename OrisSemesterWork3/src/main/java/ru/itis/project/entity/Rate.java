package ru.itis.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Rate {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Skill skill;

    @ManyToOne
    private User rater;

    private Integer rate;

    private String message;
}
