package ru.itis.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.project.dto.NotificationDto;
import ru.itis.project.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationDtoMapper {

    @Mapping(target = "fromUsername", source = "from.username")
    @Mapping(target = "skillId", source = "skill.id")
    @Mapping(target = "skillName", source = "skill.name")
    @Mapping(target = "startDateTime", source = "lesson.startDateTime")
    @Mapping(target = "lessonId", source = "lesson.id")
    NotificationDto notificationToNotificationDto(Notification notification);
}
