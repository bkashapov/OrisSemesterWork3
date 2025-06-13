package ru.itis.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.itis.project.dictionary.NotificationStatus;
import ru.itis.project.dictionary.NotificationType;

import java.time.LocalDateTime;

@Entity
@Data
@Accessors(chain = true)
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User from;

    @ManyToOne
    private User to;

    @ManyToOne
    private Skill skill;

    @Enumerated
    private NotificationType type;

    private String message;

    @Enumerated
    private NotificationStatus status;

    private LocalDateTime createdAt;
}
