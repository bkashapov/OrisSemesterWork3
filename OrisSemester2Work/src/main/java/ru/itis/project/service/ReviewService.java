package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.project.dictionary.LessonStatus;
import ru.itis.project.dto.RateDto;
import ru.itis.project.dto.RateFormDto;
import ru.itis.project.entity.Rate;
import ru.itis.project.entity.Skill;
import ru.itis.project.entity.User;
import ru.itis.project.exception.BadRequestException;
import ru.itis.project.exception.NoRightsException;
import ru.itis.project.exception.UserNotFoundException;
import ru.itis.project.mapper.RateDtoMapper;
import ru.itis.project.repository.LessonRepository;
import ru.itis.project.repository.RateRepository;
import ru.itis.project.repository.SkillRepository;
import ru.itis.project.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final LessonRepository lessonRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final RateRepository rateRepository;
    private final RateDtoMapper rateDtoMapper;

    public RateDto addRate(String username, Long skillId, RateFormDto rateDto, String authenticatedUsername) {
        Skill skill = skillRepository.findById(skillId);
        User user = userRepository.findByUsername(authenticatedUsername).orElseThrow(() -> new UserNotFoundException(authenticatedUsername));
        if (!skill.getUser().getUsername().equals(username)) {
            throw new BadRequestException(skill.getUser().getUsername() + "not equal to " + username);
        }
        if (lessonRepository.
                existsByTeacherSkillIdAndStudentIdAndStatus(skillId, user.getId(), LessonStatus.COMPLETE) ||
                rateRepository.existsBySkillIdAndRaterUsername(skillId, authenticatedUsername)) {
            throw new NoRightsException();
        }
        Rate rate = rateDtoMapper.toEntity(rateDto, skill, user);
        rateRepository.save(rate);
        Double newRate = (skill.getRating() * skill.getRatingCount() + rateDto.rate()) / (skill.getRatingCount() + 1);
        

        skillRepository.updateRatingAndRatingCount(skill.getId(), newRate, skill.getRatingCount() + 1);
        return rateDtoMapper.toDto(rate);
    }

    public Page<RateDto> getRatesOfSkill(String username, Long skillId, int pageNum, int pageSize) {
        Skill skill = skillRepository.findById(skillId);
        if (!skill.getUser().getUsername().equals(username)) {
            throw new BadRequestException(skill.getUser().getUsername() + "not equal to " + username);
        }
        return rateRepository.findAllBySkillId(
                        skillId,
                        PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "rate")))
                .map(rateDtoMapper::toDto);
    }
}
