package ru.itis.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.project.dto.SkillCreateDto;
import ru.itis.project.dto.UserPageDto;
import ru.itis.project.service.HomepageService;
import ru.itis.project.service.LessonService;
import ru.itis.project.service.NotificationService;
import ru.itis.project.service.SkillService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/me")
public class MeController {

    private final HomepageService homepageService;
    private final SkillService skillService;
    private final LessonService lessonService;
    private final NotificationService notificationService;

    @GetMapping
    public String homepage(@AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        UserPageDto userPageDto = homepageService.getHomepage(userDetails.getUsername());
        model.addAttribute("user", userPageDto.user());
        model.addAttribute("skills", userPageDto.skills());
        return "me";
    }
    @GetMapping("/skill/{skillId}")
    public String getMySkill(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable Long skillId,
                               Model model) {
        model.addAttribute("skill", skillService.getSkill(userDetails.getUsername(), skillId));
        return "my-skill";
    }

    @GetMapping("/skill")
    public String getMySkills(@AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        model.addAttribute("skillUsername", userDetails.getUsername());
        return "my-skills";
    }

    @GetMapping("/skill/create-form")
    public String createSkillForm(@AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        model.addAttribute("skill", new SkillCreateDto());
        return "skill-create";
    }

    @GetMapping("/lesson")
    public String getMyLessons(@AuthenticationPrincipal UserDetails userDetails) {
        return "lessons";
    }

    @GetMapping("/request")
    public String getRequests(@AuthenticationPrincipal UserDetails userDetails) {
        return "requests";
    }


}
