package ru.itis.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SkillCreateDto {
    @NotNull
    String name;
    @NotNull
    String category;
    @Size(max = 600)
    String description;
    MultipartFile file;
}
