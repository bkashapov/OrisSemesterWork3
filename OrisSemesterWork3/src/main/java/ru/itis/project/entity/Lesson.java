package ru.itis.project.entity;

import ru.itis.project.dictionary.LessonStatus;

import javax.persistence.*;

@Entity
public class Lesson {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User teacher;

    @ManyToOne
    private User student;

    @ManyToOne
    private Skill teacherSkill;

    @Enumerated
    private LessonStatus status;
}
