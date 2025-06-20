package ru.itis.project.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.itis.project.dictionary.LessonStatus;
import ru.itis.project.dictionary.NotificationStatus;
import ru.itis.project.dictionary.NotificationType;
import ru.itis.project.dto.LessonDto;
import ru.itis.project.dto.NotificationDto;
import ru.itis.project.dto.SkillCreateDto;
import ru.itis.project.service.LessonService;
import ru.itis.project.service.NotificationService;
import ru.itis.project.service.SkillScheduleService;
import ru.itis.project.service.SkillService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class MeRestController {

    private final SkillService skillService;
    private final SkillScheduleService skillScheduleService;
    private final LessonService lessonService;
    private final NotificationService notificationService;


    @PostMapping("/skill")
    public Long addSkill(@AuthenticationPrincipal UserDetails userDetails,
                             @ModelAttribute SkillCreateDto skillDto) {
        return skillService.add(userDetails.getUsername(), skillDto).id();
    }

    @GetMapping("/skill/{skillId}/schedule")
    public List<String> getSkillSchedules(@PathVariable Long skillId,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        return skillScheduleService.getAll(userDetails.getUsername(), skillId);
    }

    @PostMapping("/skill/{skillId}/schedule")
    public void addSkillSchedules(@PathVariable Long skillId,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody List<String> skillScheduleList) {
        skillScheduleService.saveAll(skillScheduleList, skillId);
    }

    @PatchMapping("/skill/{skillId}/schedule")
    public void updateSkillSchedules(@PathVariable Long skillId,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody List<String> skillScheduleList) {
        skillScheduleService.update(skillScheduleList, skillId);
    }

    @GetMapping("/lesson")
    public List<LessonDto> getLessons(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam LocalDate upperBoundDate,
                                      @RequestParam LocalDate lowerBoundDate) {
        return lessonService.getLessons(userDetails.getUsername(), upperBoundDate, lowerBoundDate);
    }

    @PatchMapping("lesson/{lessonId}")
    public LessonDto updateLessonStatus(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable Long lessonId,
                                        @RequestParam LessonStatus status) {
        return lessonService.updateLessonStatus(userDetails.getUsername(), lessonId, status);
    }

    @GetMapping("/notification")
    public Page<NotificationDto> getNotifications(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestParam NotificationType type,
                                                  @RequestParam NotificationStatus status,
                                                  @RequestParam int pageNum,
                                                  @RequestParam int pageSize) {
        return notificationService.getNotifications(userDetails, pageNum, pageSize, status, type);
    }

    @PostMapping("/request/{notificationId}/response")
    public void updateNotificationStatus(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable Long notificationId,
                                         @RequestParam boolean accept) {
        notificationService.handleResponse(userDetails.getUsername(), notificationId, accept);

    }
}
