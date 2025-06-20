package ru.itis.project.dto;

import ru.itis.project.dictionary.LessonStatus;

import java.time.LocalDateTime;

public record LessonDto (
        Long id,
        String teacherUsername,
        String studentUsername,
        LessonStatus status,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String skillName,
        Long skillId,
        String studentZoomUrl,
        String teacherZoomUrl
) {}    
