package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.project.dictionary.LessonStatus;
import ru.itis.project.dto.LessonDto;
import ru.itis.project.entity.Lesson;
import ru.itis.project.exception.NoRightsException;
import ru.itis.project.mapper.LessonDtoMapper;
import ru.itis.project.repository.LessonRepository;
import ru.itis.project.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonDtoMapper lessonDtoMapper;

    public List<LessonDto> getLessons(String username, int pageNum, int pageSize) {
        return lessonDtoMapper.toDto(
                lessonRepository.findAllByTeacherUsernameAndStudentUsername(
                        username,
                        PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "startDateTime"))));
    }

    public void updateLessonStatus(String username, Long lessonId, LessonStatus status) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(ResourceNotFoundException::new);
        if (!lesson.getTeacher().getUsername().equals(username) ||
                lesson.getStatus().compareTo(status) > 0) {
            throw new NoRightsException();
        }
        lesson.setStatus(status);
        lessonRepository.updateStatus(lesson.getId(), status);
    }
}
