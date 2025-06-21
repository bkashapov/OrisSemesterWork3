package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.project.entity.Skill;
import ru.itis.project.exception.NoRightsException;
import ru.itis.project.repository.SkillRepository;
import ru.itis.project.repository.SkillScheduleRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SkillScheduleService {

    private final SkillScheduleRepository skillScheduleRepository;
    private final SkillRepository skillRepository;
    private final ScheduleParserService scheduleParserService;
    private final ScheduleFormatterService scheduleFormatterService;

    public void saveAll(List<String> skillSchedulePatterns, Long skillId) {
        skillScheduleRepository
                .saveAll(
                        scheduleParserService.parseAll(skillSchedulePatterns, new Skill().setId(skillId))
                );
    }

    public List<String> getAll(String username, Long skillId) {
        Skill skill = skillRepository.findById(skillId);
        if (!Objects.equals(username, skill.getUser().getUsername())) {
            throw new NoRightsException("Not your skill");
        }
        return scheduleFormatterService.formatAll(skillScheduleRepository.findAllBySkillId(skillId));
    }

    public void update(List<String> skillScheduleList, Long skillId) {
        skillScheduleRepository.deleteAllBySkillId(skillId);
        skillScheduleRepository
                .saveAll(
                        scheduleParserService.parseAll(skillScheduleList, new Skill().setId(skillId))
                );
        log.info("skill schedule updated");
    }
}
