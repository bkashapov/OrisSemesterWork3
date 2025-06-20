package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.project.dictionary.LessonStatus;
import ru.itis.project.dictionary.NotificationStatus;
import ru.itis.project.dictionary.NotificationType;
import ru.itis.project.dto.NotificationDto;
import ru.itis.project.entity.Notification;
import ru.itis.project.exception.NoRightsException;
import ru.itis.project.exception.ResourceNotFoundException;
import ru.itis.project.mapper.NotificationDtoMapper;
import ru.itis.project.repository.LessonRepository;
import ru.itis.project.repository.NotificationRepository;
import ru.itis.project.repository.UserRepository;
import ru.itis.project.validator.LessonCanBeAcceptedValidator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final LessonRepository lessonRepository;
    private final NotificationDtoMapper notificationDtoMapper;
    private final LessonCanBeAcceptedValidator lessonCanBeAcceptedValidator;
    private final UserRepository userRepository;

    public Page<NotificationDto> getNotifications(UserDetails userDetails, int pageNum, int pageSize, NotificationStatus status, NotificationType type) {
        return notificationRepository.findNotificationByToUsernameAndStatusAndType(userDetails.getUsername(),
                status, type,
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(notificationDtoMapper::notificationToNotificationDto);
    }

    public void updateNotificationStatus(UserDetails userDetails, Long id, NotificationStatus status) {
        Notification notification = notificationRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        if (!notification.getTo().getUsername().equals(userDetails.getUsername())) {
            throw new NoRightsException("This is not your notification");
        }

        notification.setStatus(status);
        notificationRepository.save(notification);
    }

    public void handleResponse(String username, Long notificationId, boolean accept) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(ResourceNotFoundException::new);
        if (!notification.getTo().getUsername().equals(username)) {
            throw new NoRightsException("This is not your notification");
        }
        if (!notification.getType().equals(NotificationType.REQUEST)) {
            throw new NoRightsException("This is not request notification");
        }
        if (!lessonCanBeAcceptedValidator.validate(notification.getLesson()) && accept) {
            throw new NoRightsException("This is not accepted lesson");
        }
        LessonStatus lessonStatus = accept ? LessonStatus.PLANNED : LessonStatus.REJECTED;
        notification.getLesson().setStatus(lessonStatus);
        lessonRepository.save(notification.getLesson());
        if (lessonStatus.equals(LessonStatus.REJECTED)) {
            notification.getLesson().getStudent().setSkillPoints(notification.getLesson().getStudent().getSkillPoints() + 1);
            userRepository.save(notification.getLesson().getStudent());
        }

        notification.setStatus(NotificationStatus.READ);
        notificationRepository.save(notification);
        Notification notification1 = new Notification()
                .setFrom(notification.getLesson().getTeacher())
                .setTo(notification.getLesson().getStudent())
                .setType(NotificationType.RESPONSE)
                .setSkill(notification.getSkill())
                .setCreatedAt(LocalDateTime.now())
                .setMessage(String.format("Your request has been processed: %s", accept ? "ACCEPTED" : "REJECTED"));
        notificationRepository.save(notification1);
    }
}
