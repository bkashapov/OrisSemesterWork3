package ru.itis.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {
    @NotNull
    @NotBlank
    String username;
    @NotNull
    @NotBlank
    String password;
}
