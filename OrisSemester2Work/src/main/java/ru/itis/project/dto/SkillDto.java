package ru.itis.project.dto;

public record SkillDto (
    Long id,
    String name,
    String username,
    Double rating,
    Integer ratingCount,
    String category,
    String description,
    String imageFilename,
    boolean isArchived,
    Integer lessonCount,
    Integer lectureCount,
    Integer commentCount
) {}