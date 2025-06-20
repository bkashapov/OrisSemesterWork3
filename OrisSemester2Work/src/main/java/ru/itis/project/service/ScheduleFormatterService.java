package ru.itis.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.project.entity.SkillSchedule;
import ru.itis.project.formatter.ScheduleFormatter;

import java.util.List;

@Service
public class ScheduleFormatterService {

    private final List<ScheduleFormatter> formatters;

    @Autowired
    public ScheduleFormatterService(List<ScheduleFormatter> formatters) {
        this.formatters = formatters;
    }

    public String format(SkillSchedule schedule) {
        return formatters.stream()
                .filter(f -> f.supports(schedule.getType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No formatter for type: " + schedule.getType()))
                .format(schedule);
    }

    public List<String> formatAll(List<SkillSchedule> schedules) {
        return schedules.stream()
                .map(this::format)
                .toList();
    }
}

