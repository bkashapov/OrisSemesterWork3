package ru.itis.project.formatter;

import org.springframework.stereotype.Component;
import ru.itis.project.dictionary.SkillScheduleType;
import ru.itis.project.entity.SkillSchedule;

import java.util.Locale;

@Component
public class SingleDateScheduleFormatter implements ScheduleFormatter {

    @Override
    public boolean supports(SkillScheduleType type) {
        return type == SkillScheduleType.SINGLE_DATE;
    }

    @Override
    public String format(SkillSchedule schedule) {
        return String.format("single-date %s %s %s",
                schedule.getSingleDate(),
                schedule.getStartTime(),
                formatDuration(schedule.getLessonLength()));
    }

    private String formatDuration(int minutes) {
        return String.format(Locale.US, "%.1fh", minutes / 60.0);
    }
}

