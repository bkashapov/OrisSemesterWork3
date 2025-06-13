package ru.itis.project.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.itis.project.validator.ExistInDbValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ExistInDbValidator.class)
public @interface ExistInDb {
    Class<?> repositoryClass();
    String methodName();
    Class<?>[] parameterTypes();
    boolean existing() default true;
    String message() default "Users do not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
