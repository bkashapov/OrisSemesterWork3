package ru.itis.project.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CommentClientDto {
    private Long id;

    private Long authorId;

    private Long skillId;

    private String text;

    private int likes;

    private LocalDateTime createdAt;
}
