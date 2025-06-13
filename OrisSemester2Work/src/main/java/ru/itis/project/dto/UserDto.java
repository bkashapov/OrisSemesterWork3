package ru.itis.project.dto;

public record UserDto (
    String username,
    Double avgRating,
    Integer skillPoints,
    String email,
    String description
) {}
