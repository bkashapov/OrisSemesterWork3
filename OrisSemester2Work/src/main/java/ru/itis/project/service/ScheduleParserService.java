package ru.itis.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.project.entity.Skill;
import ru.itis.project.entity.SkillSchedule;
import ru.itis.project.parser.ScheduleParser;

import java.util.List;

@Service
public class ScheduleParserService {

    private final List<ScheduleParser> parsers;

    @Autowired
    public ScheduleParserService(List<ScheduleParser> parsers) {
        this.parsers = parsers;
    }

    public SkillSchedule parseLine(String line, Skill skill) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 4) throw new IllegalArgumentException("Too short line: " + line);

        String type = parts[0];

        return parsers.stream()
                .filter(p -> p.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No parser for type: " + type))
                .parse(parts, skill);
    }

    public List<SkillSchedule> parseAll(List<String> lines, Skill skill) {
        return lines.stream()
                .map(line -> parseLine(line, skill))
                .toList();
    }
}
