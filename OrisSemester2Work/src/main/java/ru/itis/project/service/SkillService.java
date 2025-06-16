package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.project.dictionary.LessonStatus;
import ru.itis.project.dto.*;
import ru.itis.project.entity.*;
import ru.itis.project.exception.BadRequestException;
import ru.itis.project.exception.NoRightsException;
import ru.itis.project.exception.UserNotFoundException;
import ru.itis.project.mapper.RateDtoMapper;
import ru.itis.project.mapper.SkillDtoMapper;
import ru.itis.project.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final LessonRepository lessonRepository;
    private final LectureRepository lectureRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RateRepository rateRepository;
    private final SkillDtoMapper skillDtoMapper;
    private final RateDtoMapper rateDtoMapper;
    private final ImageService imageService;


    public List<SkillBasicDto> getSkills(String username, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "avgRating"));
        return skillRepository.findAllByUsername(username, pageable).stream().map(skillDtoMapper::toSkillBasicDto).toList();
    }

    public SkillDto getSkill(String username, Long skillId) {
        Skill skill = skillRepository.findById(skillId);
        verify(skill, username);
        return skillDtoMapper
                .toSkillDto(
                        skill,
                        lessonRepository.countAllByTeacherSkillId(skillId),
                        lectureRepository.countAllBySkillId(skillId),
                        commentRepository.countAllBySkillId(skillId));
    }

    @Transactional
    public SkillDto add(String username, SkillCreateDto skillDto) {
        Optional<Category> optionalCategory = categoryRepository.
                findByName(skillDto.getCategory().toLowerCase());
        if (optionalCategory.isEmpty()) {
            optionalCategory = Optional.of(new Category().setName(skillDto.getCategory()));
            categoryRepository.save(optionalCategory.get());
        }
        Category category = optionalCategory.get();
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Skill skill = skillDtoMapper.toSkill(skillDto, category, user);
        skill.setImageFilename(skillDto.getFile() != null ? imageService.saveMultipartFile(skillDto.getFile()) : ImageService.DEFAULT_IMAGE_FILENAME);
        skillRepository.save(skill);
        return skillDtoMapper.toSkillDto(skill, 0, 0, 0);
    }

    public boolean signUp(String username, int skillId, UserDetails authenticatedUser) {
        Skill skill = skillRepository.findById(skillId);
        User teacher = verify(skill, username);
        User student = userRepository.findByUsername(authenticatedUser.getUsername()).orElseThrow(() -> new UserNotFoundException(authenticatedUser.getUsername()));
        Lesson lesson = new Lesson()
                .setTeacher(teacher)
                .setStudent(student)
                .setStatus(LessonStatus.PENDED);

        lessonRepository.save(lesson);
        return true;
    }

    private User verify(Skill skill, String username) {
        if (!skill.getUser().getUsername().equals(username)) {
            throw new BadRequestException();
        }
        return skill.getUser();
    }

    public List<RateDto> getRatesOfSkill(String username, Long skillId, int pageNum, int pageSize) {
        Skill skill = skillRepository.findById(skillId);
        verify(skill, username);
        return rateDtoMapper.toDto(
                rateRepository.findAllBySkillId(
                        skillId,
                        PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, "rating"))));
    }

    public void addRate(String username, Long skillId, RateFormDto rateDto, String authenticatedUsername) {
        Skill skill = skillRepository.findById(skillId);
        verify(skill, username);
        if (lessonRepository.
                findAllByTeacherUsernameAndStudentUsernameAndStatus(authenticatedUsername, LessonStatus.COMPLETE).isEmpty() ||
        rateRepository.findBySkillIdAndRaterUsername(skillId, authenticatedUsername).isPresent()) {
            throw new NoRightsException();
        }
        User user = userRepository.findByUsername(authenticatedUsername).orElseThrow(UserNotFoundException::new);
        Rate rate = rateDtoMapper.toEntity(rateDto)
                .setSkill(skill)
                .setRater(user);
        rateRepository.save(rate);
        Double newRate = (skill.getRating() * skill.getRatingCount() + rateDto.rate()) / skill.getRatingCount();

        skillRepository.updateRatingAndRatingCount(skill.getId(), newRate, skill.getRatingCount() + 1);
    }
}
