package ru.itis.project.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.project.exception.NoRightsException;
import ru.itis.project.exception.UserNotFoundException;

@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoRightsException.class)
    public ResponseEntity<Object> handleNoRightsException(
            Exception ex
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}
