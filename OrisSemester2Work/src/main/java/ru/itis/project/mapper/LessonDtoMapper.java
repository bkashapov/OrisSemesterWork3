package ru.itis.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.project.dto.LessonDto;
import ru.itis.project.entity.Lesson;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonDtoMapper {

    List<LessonDto> toDto(List<Lesson> lessons);

    @Mapping(target = "skillId", source = "teacherSkill.id")
    @Mapping(target = "skillName", source = "teacherSkill.name")
    @Mapping(target = "studentUsername", source = "student.username")
    @Mapping(target = "teacherUsername", source = "teacher.username")
    LessonDto toDto(Lesson lesson);

}
