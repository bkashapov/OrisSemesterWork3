package ru.itis.project.entity;

import ru.itis.project.dictionary.NotificationStatus;
import ru.itis.project.dictionary.NotificationType;

import javax.persistence.*;

@Entity
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
}
