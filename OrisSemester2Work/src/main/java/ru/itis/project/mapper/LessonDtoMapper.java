package ru.itis.project.mapper;

import org.mapstruct.Mapper;
import ru.itis.project.dto.LessonDto;
import ru.itis.project.entity.Lesson;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonDtoMapper {

    List<LessonDto> toDto(List<Lesson> lessons);

}
