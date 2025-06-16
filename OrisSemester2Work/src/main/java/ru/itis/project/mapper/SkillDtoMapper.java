package ru.itis.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.project.dto.SkillBasicDto;
import ru.itis.project.dto.SkillCreateDto;
import ru.itis.project.dto.SkillDto;
import ru.itis.project.entity.Category;
import ru.itis.project.entity.Skill;
import ru.itis.project.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillDtoMapper {

    @Mapping(target = "category", source = "skill.category.name")
    @Mapping(target = "username", source = "skill.user.username")
    @Mapping(target = "lessonCount", source = "lessonCount")
    @Mapping(target = "lectureCount", source = "lectureCount")
    SkillDto toSkillDto(Skill skill, int lessonCount, int lectureCount, int commentCount);


    List<SkillBasicDto> toSkillDtoList(List<Skill> skills);

    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "username", source = "user.username")
    SkillBasicDto toSkillBasicDto(Skill skill);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "skillCreateDto.name")
    @Mapping(target = "description", source = "skillCreateDto.description")
    @Mapping(target = "rating", expression = "java(0.0)")
    @Mapping(target = "ratingCount", expression = "java(0)")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "isArchived", expression = "java(false)")
    Skill toSkill(SkillCreateDto skillCreateDto, Category category, User user);
}
