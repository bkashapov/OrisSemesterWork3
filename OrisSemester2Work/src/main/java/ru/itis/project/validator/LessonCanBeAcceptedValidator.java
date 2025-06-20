package ru.itis.project.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.project.entity.Lesson;
import ru.itis.project.repository.LessonRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class LessonCanBeAcceptedValidator {

    private final LessonRepository lessonRepository;

    public boolean validate(Lesson newLesson) {
        List<Lesson> lessons = lessonRepository
                .findAllByTeacherAndStudentUsernamesNotPendedAndNotRejected(
                        newLesson.getTeacher().getUsername(),
                        newLesson.getStudent().getUsername(),
                        newLesson.getStartDateTime().minusDays(1L),
                        newLesson.getEndDateTime().plusDays(1L)
                );
        return lessons.stream().noneMatch(lesson -> {
            LocalDateTime maxStart = lesson.getStartDateTime().isAfter(newLesson.getStartDateTime()) ? lesson.getStartDateTime() : newLesson.getStartDateTime();
            LocalDateTime minEnd = lesson.getEndDateTime().isBefore(newLesson.getEndDateTime()) ? lesson.getEndDateTime() : newLesson.getEndDateTime();
            return maxStart.isBefore(minEnd);
        });
    }
}
