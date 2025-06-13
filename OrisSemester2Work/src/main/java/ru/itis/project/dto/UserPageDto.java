package ru.itis.project.dto;

import java.util.List;

public record UserPageDto (
    UserDto user,
    List<SkillDto> skills
) {}
