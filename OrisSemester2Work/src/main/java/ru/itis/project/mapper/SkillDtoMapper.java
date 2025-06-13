package ru.itis.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.project.dto.SkillDto;
import ru.itis.project.entity.Skill;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillDtoMapper {

    @Mapping(target = "category", source = "category.name")
    SkillDto toSkillDto(Skill skill);

    List<SkillDto> toSkillDtoList(List<Skill> skills);
}
