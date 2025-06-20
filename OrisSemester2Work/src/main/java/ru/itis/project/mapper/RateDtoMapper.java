package ru.itis.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itis.project.dto.RateDto;
import ru.itis.project.dto.RateFormDto;
import ru.itis.project.entity.Rate;
import ru.itis.project.entity.Skill;
import ru.itis.project.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RateDtoMapper {

    @Mapping(target = "raterUsername", source = "rater.username")
    RateDto toDto(Rate rate);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "skill", source = "skill")
    @Mapping(target = "rater", source = "user")
    Rate toEntity(RateFormDto dto, Skill skill, User user);
}
