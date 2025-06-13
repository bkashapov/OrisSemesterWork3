package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.itis.project.dto.NotificationDto;
import ru.itis.project.mapper.NotificationDtoMapper;
import ru.itis.project.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationDtoMapper notificationDtoMapper;

    public List<NotificationDto> getNotifications(UserDetails userDetails, int pageNum, int pageSize) {
        return notificationRepository.findNotificationByToUsername(userDetails.getUsername(),
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "startDateTime")))
                .stream()
                .map(notificationDtoMapper::notificationToNotificationDto)
                .toList();
    }
}
