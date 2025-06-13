package ru.itis.project.dto;

public record RateDto (
    String raterUsername,
    int rate,
    String message
) {}
