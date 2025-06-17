package ru.itis.project.dto;

import lombok.Data;

@Data
public class SkillBasicDto {
    Long id;
    String name;
    String username;
    Double rating;
    Integer ratingCount;
    String category;
    String description;
    String imageFilename;
    boolean isArchived;

}
