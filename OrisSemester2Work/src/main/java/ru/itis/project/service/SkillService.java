package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.project.dictionary.LessonStatus;
import ru.itis.project.dto.SkillBasicDto;
import ru.itis.project.dto.SkillCreateDto;
import ru.itis.project.dto.SkillDto;
import ru.itis.project.entity.Category;
import ru.itis.project.entity.Lesson;
import ru.itis.project.entity.Skill;
import ru.itis.project.entity.User;
import ru.itis.project.exception.BadRequestException;
import ru.itis.project.exception.UserNotFoundException;
import ru.itis.project.mapper.RateDtoMapper;
import ru.itis.project.mapper.SkillDtoMapper;
import ru.itis.project.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final LessonRepository lessonRepository;
    private final LectureRepository lectureRepository;
    private final CommentService commentService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SkillDtoMapper skillDtoMapper;
    private final ImageService imageService;

    public List<SkillBasicDto> getSkills() {
        Pageable pageable = PageRequest.of(0, 16, Sort.by(Sort.Direction.ASC, "rating"));
        return skillRepository.getSkills(pageable).stream().map(skillDtoMapper::toSkillBasicDto).toList();
    }

    public List<SkillBasicDto> getSkills(String username, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "rating"));
        return skillRepository.findAllByUsername(username, pageable).stream().map(skillDtoMapper::toSkillBasicDto).toList();
    }

    public SkillDto getSkill(String username, Long skillId) {
        Skill skill = skillRepository.findById(skillId);
        verify(skill, username);
        return skillDtoMapper
                .toSkillDto(
                        skill,
                        lessonRepository.countCompleteByTeacherSkillId(skillId),
                        lectureRepository.countAllBySkillId(skillId),
                        commentService.getCount(skillId));
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
        skill.setImageFilename(skillDto.getFile() != null && !skillDto.getFile().isEmpty() ? imageService.saveMultipartFile(skillDto.getFile()) : ImageService.DEFAULT_IMAGE_FILENAME);
        skillRepository.save(skill);
        log.info("Skill created");
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
        log.info("User signed up to the lesson");
        return true;
    }

    private User verify(Skill skill, String username) {
        if (!skill.getUser().getUsername().equals(username)) {
            log.info("Skill creator username not equal auth username");
            throw new BadRequestException(skill.getUser().getUsername() + "not equal to " + username);
        }
        return skill.getUser();
    }


    public Page<SkillBasicDto> getSkillsByUsernameAndQuery(String username, String query, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, "rating"));
        String pattern = '%' + query + '%';
        Page<Skill> skills =  skillRepository.findAllByUsernameAndQuery(username, pattern, pageable);
        return skills.map(skillDtoMapper::toSkillBasicDto);
    }
}
