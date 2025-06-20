package ru.itis.project.parser;

import org.springframework.stereotype.Component;
import ru.itis.project.dictionary.SkillScheduleType;
import ru.itis.project.entity.Skill;
import ru.itis.project.entity.SkillSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class SingleDateScheduleParser implements ScheduleParser {

    @Override
    public boolean supports(String type) {
        return "single-date".equalsIgnoreCase(type);
    }

    @Override
    public SkillSchedule parse(String[] parts, Skill skill) {
        if (parts.length < 4) throw new IllegalArgumentException("Invalid single-date format");

        SkillSchedule schedule = new SkillSchedule();
        schedule.setSkill(skill);
        schedule.setType(SkillScheduleType.SINGLE_DATE);

        schedule.setSingleDate(LocalDate.parse(parts[1]));
        schedule.setStartTime(LocalTime.parse(parts[2]));
        schedule.setLessonLength(parseDuration(parts[3]));

        return schedule;
    }

    private int parseDuration(String str) {
        return (int) (Double.parseDouble(str.replace("h", "")) * 60);
    }
}
