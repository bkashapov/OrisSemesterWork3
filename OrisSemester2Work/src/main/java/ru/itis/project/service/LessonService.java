package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.project.dictionary.LessonStatus;
import ru.itis.project.dictionary.NotificationType;
import ru.itis.project.dto.LessonDto;
import ru.itis.project.dto.LessonSlotDto;
import ru.itis.project.dto.ZoomUrlDto;
import ru.itis.project.entity.*;
import ru.itis.project.exception.NoRightsException;
import ru.itis.project.exception.ResourceNotFoundException;
import ru.itis.project.exception.UserNotFoundException;
import ru.itis.project.mapper.LessonDtoMapper;
import ru.itis.project.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonDtoMapper lessonDtoMapper;
    private final SkillRepository skillRepository;
    private final SkillScheduleRepository skillScheduleRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ZoomService zoomService;

    public List<LessonDto> getLessons(String username, LocalDate upperBoundDate, LocalDate lowerBoundDate) {
        List<Lesson> lessons = lessonRepository.findAllByTeacherAndStudentUsernameNotPended(
                username,
                lowerBoundDate.atTime(LocalTime.MIDNIGHT),
                upperBoundDate.atTime(LocalTime.MIDNIGHT)
        );
        return lessons.stream().map(lessonDtoMapper::toDto).toList();
    }

    public List<LessonSlotDto> getFreeTimes(String username, Long skillId, LocalDateTime endTime) {
        Skill skill = skillRepository.findById(skillId);
        if (skill.getUser().getUsername().equals(username)) {
            throw new NoRightsException("You have no rights to enroll this lesson");
        }
        List<Lesson> lessons = lessonRepository
                .findAllByTeacherAndStudentUsernamesNotPendedAndNotRejected(
                        skill.getUser().getUsername(),
                        username,
                        LocalDateTime.now(),
                        endTime
                );

        List<SkillSchedule> schedules = skillScheduleRepository.findUpcomingOrUnboundSchedules(skillId, LocalDate.now());

        List<LessonSlotDto> availableSlots = new ArrayList<>();

        LocalDateTime cur = LocalDateTime.now();

        while (cur.isBefore(endTime)) {
            for (SkillSchedule schedule : schedules) {
                LocalDateTime slotStart = null;

                switch (schedule.getType()) {
                    case WEEKLY:
                        if (cur.getDayOfWeek() == schedule.getDayOfWeek()) {
                            slotStart = cur.toLocalDate().atTime(schedule.getStartTime());
                        }
                        break;
                    case MONTHLY:
                        if (cur.getDayOfMonth() == schedule.getMonthDay()) {
                            slotStart = cur.toLocalDate().atTime(schedule.getStartTime());
                        }
                        break;
                    case SINGLE_DATE:
                        if (cur.toLocalDate().equals(schedule.getSingleDate())) {
                            slotStart = schedule.getSingleDate().atTime(schedule.getStartTime());
                        }
                        break;
                }

                if (slotStart != null && !slotStart.isBefore(LocalDateTime.now())) {
                    LocalDateTime slotEnd = slotStart.plusMinutes(schedule.getLessonLength());

                    LocalDateTime finalSlotStart = slotStart;
                    boolean overlaps = lessons.stream().anyMatch(lesson -> {
                        LocalDateTime maxStart = lesson.getStartDateTime().isAfter(finalSlotStart) ? lesson.getStartDateTime() : finalSlotStart;
                        LocalDateTime minEnd = lesson.getEndDateTime().isBefore(slotEnd) ? lesson.getEndDateTime() : slotEnd;
                        return maxStart.isBefore(minEnd);
                    });

                    if (!overlaps && slotEnd.isBefore(endTime)) {
                        availableSlots.add(new LessonSlotDto().setStart(slotStart).setLengthMinutes(schedule.getLessonLength()));
                    }
                }
            }

            cur = cur.plusDays(1);
        }

        return availableSlots;

    }


    public LessonDto updateLessonStatus(String username, Long lessonId, LessonStatus status) {

        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(ResourceNotFoundException::new);
        User student = lesson.getStudent();
        if (!lesson.getTeacher().getUsername().equals(username) ||
                lesson.getStatus().compareTo(status) > 0) {
            throw new NoRightsException();
        }
        Notification notification = new Notification()
                .setType(NotificationType.STATUS_UPDATE)
                .setFrom(lesson.getTeacher())
                .setTo(lesson.getStudent())
                .setSkill(lesson.getTeacherSkill())
                .setCreatedAt(LocalDateTime.now())
                .setLesson(lesson)
                .setMessage(String.format("Lesson '%s' status updated from %s to %s", lesson.getTeacherSkill().getName(), lesson.getStatus(), status));

        lesson.setStatus(status);
        if (status.equals(LessonStatus.ACTIVE)) {
            ZoomUrlDto zoomUrl = zoomService.createInstantMeeting(username);
            lesson.setTeacherZoomUrl(zoomUrl.getStartUrl());
            lesson.setStudentZoomUrl(zoomUrl.getJoinUrl());
        } else if (status.equals(LessonStatus.COMPLETE)) {
            lesson.setStudentZoomUrl("");
            lesson.setTeacherZoomUrl("");
            lesson.getTeacher().setSkillPoints(lesson.getTeacher().getSkillPoints() + 1);
            userRepository.save(lesson.getStudent());
            userRepository.save(lesson.getTeacher());
        }
        lessonRepository.save(lesson);
        notificationRepository.save(notification);
        return lessonDtoMapper.toDto(lesson);
    }

    public void signUp(String username, String teacherUsername, Long skillId, LessonSlotDto slotDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Not authenticated"));
        Skill skill = skillRepository.findById(skillId);
        if (lessonRepository
                .existsByTeacherSkillIdAndStudentIdAndStatus(
                        skillId,
                        user.getId(),
                        LessonStatus.PENDED
                ) ||
        lessonRepository
                .existsByTeacherSkillIdAndStudentIdAndStatus(
                        skillId,
                        user.getId(),
                        LessonStatus.PLANNED
                )) {
            throw new NoRightsException("You have already enrolled this skill. Wait until you complete previous lesson");
        }
        LocalDateTime startDateTime = slotDto.getStart();
        LocalDateTime endDateTime = startDateTime.plusMinutes(slotDto.getLengthMinutes());
        Lesson lesson = new Lesson()
                .setStatus(LessonStatus.PENDED)
                .setTeacher(skill.getUser())
                .setStudent(user)
                .setStartDateTime(startDateTime)
                .setEndDateTime(endDateTime)
                .setTeacherSkill(skill);
        lessonRepository.save(lesson);

        user.setSkillPoints(user.getSkillPoints() - 1);
        userRepository.save(user);

        Notification notification = new Notification()
                .setFrom(user)
                .setTo(skill.getUser())
                .setSkill(skill)
                .setType(NotificationType.REQUEST)
                .setMessage("You have been sent a request for a skill")
                .setCreatedAt(LocalDateTime.now())
                .setLesson(lesson);
        notificationRepository.save(notification);
    }
}
