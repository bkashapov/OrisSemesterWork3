package ru.itis.project.exception;

public class NoRightsException extends RuntimeException {
    public NoRightsException() {}
    public NoRightsException(String message) {
        super(message);
    }
}
