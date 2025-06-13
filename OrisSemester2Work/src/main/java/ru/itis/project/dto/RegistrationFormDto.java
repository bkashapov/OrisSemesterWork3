package ru.itis.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.itis.project.annotation.validation.ExistInDb;
import ru.itis.project.repository.UserRepository;

@Data
public class RegistrationFormDto {
    @NotNull
    @Size(min = 8, max = 20)
    @ExistInDb(
            repositoryClass = UserRepository.class,
            methodName = "existsByUsername",
            parameterTypes = {String.class},
            existing = false
    )
    @Pattern(regexp = "^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$", message = "Invalid username")
    String username;
    @NotNull
    @Size(min = 6, max = 50)
    @ExistInDb(
            repositoryClass = UserRepository.class,
            methodName = "existsByEmail",
            parameterTypes = {String.class},
            existing = false
    )
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email")
    String email;
    @NotNull
    @Size(min = 6, max = 30)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Invalid password")
    String password;
    @Size(max = 400)
    String description;
}
