package ru.itis.project.dto;
public record SkillCreateDto (
     String name,
     String category,
     String description,
     byte[] file
) {}
