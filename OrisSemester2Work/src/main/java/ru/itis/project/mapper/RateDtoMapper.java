package ru.itis.project.mapper;

import org.mapstruct.Mapper;
import ru.itis.project.dto.RateDto;
import ru.itis.project.dto.RateFormDto;
import ru.itis.project.entity.Rate;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RateDtoMapper {

    List<RateDto> toDto(List<Rate> rates);

    Rate toEntity(RateFormDto dto);
}
