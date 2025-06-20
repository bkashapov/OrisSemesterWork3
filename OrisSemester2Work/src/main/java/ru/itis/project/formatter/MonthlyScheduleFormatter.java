package ru.itis.project.formatter;

import org.springframework.stereotype.Component;
import ru.itis.project.dictionary.SkillScheduleType;
import ru.itis.project.entity.SkillSchedule;

import java.util.Locale;

@Component
public class MonthlyScheduleFormatter implements ScheduleFormatter {

    @Override
    public boolean supports(SkillScheduleType type) {
        return type == SkillScheduleType.MONTHLY;
    }

    @Override
    public String format(SkillSchedule schedule) {
        return String.format("monthly %dd %s %s",
                schedule.getMonthDay(),
                schedule.getStartTime(),
                formatDuration(schedule.getLessonLength()));
    }

    private String formatDuration(int minutes) {
        return String.format(Locale.US, "%.1fh", minutes / 60.0);
    }
}
