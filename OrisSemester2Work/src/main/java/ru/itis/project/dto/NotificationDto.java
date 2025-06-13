package ru.itis.project.dto;

import ru.itis.project.dictionary.NotificationStatus;
import ru.itis.project.dictionary.NotificationType;

import java.time.LocalDateTime;

public record NotificationDto (
    Long id,
    NotificationType type,
    NotificationStatus status,
    String message,
    String fromUsername,
    Long skillId,
    String skillName,
    LocalDateTime createdAt
) {}
