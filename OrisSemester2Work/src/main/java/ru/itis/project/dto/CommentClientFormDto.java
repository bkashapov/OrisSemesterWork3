package ru.itis.project.dto;

import lombok.Data;

@Data
public class CommentClientFormDto {

    private Long authorId;

    private String text;
}