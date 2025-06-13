package ru.itis.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.project.dictionary.LessonStatus;
import ru.itis.project.dto.*;
import ru.itis.project.service.HomepageService;
import ru.itis.project.service.LessonService;
import ru.itis.project.service.NotificationService;
import ru.itis.project.service.SkillService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/me")
public class MeController {

    private final HomepageService homepageService;
    private final SkillService skillService;
    private final LessonService lessonService;
    private final NotificationService notificationService;

    @GetMapping
    public String homepage(@AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        model.addAttribute("homepage", homepageService.getHomepage(userDetails));
        return "homepage";
    }

    @GetMapping("/skill")
    public List<SkillDto> getMySkills(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam int pageNum,
                                      @RequestParam int pageSize) {
        return skillService.getSkills(userDetails.getUsername(), pageNum, pageSize);
    }

    @PostMapping("/skill")
    public SkillDto addSkill(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestBody SkillCreateDto skillDto) {
        return skillService.add(userDetails.getUsername(), skillDto);
    }

    @GetMapping("/lesson")
    public List<LessonDto> getMyLessons(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam int pageNum,
                                        @RequestParam int pageSize) {
        return lessonService.getLessons(userDetails.getUsername(), pageNum, pageSize);
    }

    @PatchMapping("/lesson/{lessonId}")
    public void updateLessonStatus(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable Long lessonId,
                                   @RequestBody LessonStatus status) {
        lessonService.updateLessonStatus(userDetails.getUsername(), lessonId, status);
    }

    @GetMapping("/notification")
    public List<NotificationDto> getNotifications(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestParam int pageNum,
                                                  @RequestParam int pageSize) {
        return notificationService.getNotifications(userDetails, pageNum, pageSize);
    }


}
