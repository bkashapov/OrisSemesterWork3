package ru.itis.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
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
