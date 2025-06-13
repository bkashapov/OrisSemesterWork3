package ru.itis.project.dto;

import ru.itis.project.dictionary.LessonStatus;

public record LessonDto (
        Long id,
        String teacherUsername,
        String studentUsername,
        LessonStatus status
) {}    
