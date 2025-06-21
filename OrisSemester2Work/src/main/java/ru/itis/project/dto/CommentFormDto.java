package ru.itis.project.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentFormDto {
    private String text;
}
