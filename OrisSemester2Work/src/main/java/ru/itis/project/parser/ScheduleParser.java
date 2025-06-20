package ru.itis.project.parser;

import org.springframework.stereotype.Component;
import ru.itis.project.entity.Skill;
import ru.itis.project.entity.SkillSchedule;

public interface ScheduleParser {
    boolean supports(String type);
    SkillSchedule parse(String[] parts, Skill skill);
}