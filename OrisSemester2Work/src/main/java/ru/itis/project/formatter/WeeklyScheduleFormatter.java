package ru.itis.project.formatter;

import org.springframework.stereotype.Component;
import ru.itis.project.dictionary.SkillScheduleType;
import ru.itis.project.entity.SkillSchedule;

import java.util.Locale;

@Component
public class WeeklyScheduleFormatter implements ScheduleFormatter {

    @Override
    public boolean supports(SkillScheduleType type) {
        return type == SkillScheduleType.WEEKLY;
    }

    @Override
    public String format(SkillSchedule schedule) {
        return String.format("weekly %s %s %s",
                schedule.getDayOfWeek().toString().toLowerCase(),
                schedule.getStartTime(),
                formatDuration(schedule.getLessonLength()));
    }

    private String formatDuration(int minutes) {
        return String.format(Locale.US, "%.1fh", minutes / 60.0);
    }
}

