package ru.itis.project.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LessonSlotDto {
    private LocalDateTime start;
    private int lengthMinutes;
}
