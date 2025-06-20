package ru.itis.project.parser;

import org.springframework.stereotype.Component;
import ru.itis.project.dictionary.SkillScheduleType;
import ru.itis.project.entity.Skill;
import ru.itis.project.entity.SkillSchedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Component
public class WeeklyScheduleParser implements ScheduleParser {

    @Override
    public boolean supports(String type) {
        return "weekly".equalsIgnoreCase(type);
    }

    @Override
    public SkillSchedule parse(String[] parts, Skill skill) {
        if (parts.length < 4) throw new IllegalArgumentException("Invalid weekly format");

        SkillSchedule schedule = new SkillSchedule();
        schedule.setSkill(skill);
        schedule.setType(SkillScheduleType.WEEKLY);

        schedule.setDayOfWeek(DayOfWeek.valueOf(parts[1].toUpperCase()));
        schedule.setStartTime(LocalTime.parse(parts[2]));
        schedule.setLessonLength(parseDuration(parts[3]));

        return schedule;
    }

    private int parseDuration(String str) {
        return (int) (Double.parseDouble(str.replace("h", "")) * 60);
    }
}
