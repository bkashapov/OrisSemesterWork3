package ru.itis.project.formatter;

import ru.itis.project.dictionary.SkillScheduleType;
import ru.itis.project.entity.SkillSchedule;

public interface ScheduleFormatter {
    boolean supports(SkillScheduleType type);
    String format(SkillSchedule schedule);
}
