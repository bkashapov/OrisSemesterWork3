package ru.itis.project.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SkillCreateDto {
    String name;
    String category;
    String description;
    MultipartFile file;
}
