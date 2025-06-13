package ru.itis.project.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.itis.project.annotation.validation.ExistInDb;

import java.lang.reflect.Method;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExistInDbValidator implements ConstraintValidator<ExistInDb, Object> {

    private final ApplicationContext applicationContext;

    private Class<?> repositoryClass;
    private String methodName;
    private Class<?>[] parameterTypes;
    private boolean existing;

    @Override
    public void initialize(ExistInDb constraintAnnotation) {
        repositoryClass = constraintAnnotation.repositoryClass();
        methodName = constraintAnnotation.methodName();
        parameterTypes = constraintAnnotation.parameterTypes();
        existing = constraintAnnotation.existing();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null) { return true; }

        try {
            Object repository = applicationContext.getBean(repositoryClass);
            Method method = repository.getClass().getMethod(methodName, parameterTypes);
            Object result = method.invoke(repository, o);
            return ((Boolean) result) == existing;
        } catch (Exception e) {
            log.error("Сообщение {}: '{}'", e.getClass(), e.getMessage());
            return false;
        }
    }
}
