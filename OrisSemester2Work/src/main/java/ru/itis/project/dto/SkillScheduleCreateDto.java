package ru.itis.project.dto;

import lombok.Data;
import ru.itis.project.dictionary.SkillScheduleType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SkillScheduleCreateDto {
    private Long skillId;
    private SkillScheduleType type;
    private LocalDate singleDate;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private int lessonLength;
}
