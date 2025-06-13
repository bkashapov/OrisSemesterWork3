package ru.itis.project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.itis.project.entity.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {

    void save(Notification notification);


    //@Query("SELECT n FROM Notification n JOIN FETCH Skill s ON n.skill WHERE n.to.username = :username")
    List<Notification> findNotificationByToUsername(String username, Pageable pageable);
}
