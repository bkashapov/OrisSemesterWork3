package ru.itis.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.itis.project.dictionary.LessonStatus;

import java.time.LocalDateTime;

@Entity
@Data
@Accessors(chain = true)
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

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Enumerated
    private LessonStatus status;

    @Column(length = 600)
    private String teacherZoomUrl;

    @Column(length = 600)
    private String studentZoomUrl;
}
