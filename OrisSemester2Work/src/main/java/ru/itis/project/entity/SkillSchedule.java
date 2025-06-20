package ru.itis.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.itis.project.dictionary.SkillScheduleType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Accessors(chain = true)
public class SkillSchedule {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Skill skill;

    private SkillScheduleType type;

    private LocalDate singleDate;

    private LocalTime startTime;

    private int lessonLength;

    private int monthDay;

    private DayOfWeek dayOfWeek;
}
