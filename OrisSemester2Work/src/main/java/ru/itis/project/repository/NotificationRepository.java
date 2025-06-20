package ru.itis.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.itis.project.dictionary.NotificationStatus;
import ru.itis.project.dictionary.NotificationType;
import ru.itis.project.entity.Notification;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {

    void save(Notification notification);

    Optional<Notification> findById(Long id);


    //@Query("SELECT n FROM Notification n JOIN FETCH Skill s ON n.skill WHERE n.to.username = :username")
    Page<Notification> findNotificationByToUsernameAndStatusAndType(String username, NotificationStatus status, NotificationType type, Pageable pageable);
}
