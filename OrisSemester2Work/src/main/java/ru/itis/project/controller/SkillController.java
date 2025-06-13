package ru.itis.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.project.dto.RateFormDto;
import ru.itis.project.service.SkillService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/{username}/skill")
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public String getSkills(@PathVariable String username,
                                    @RequestParam int pageNum,
                                    @RequestParam int pageSize,
                                    Model model) {
        model.addAttribute("skills", skillService.getSkills(username, pageNum, pageSize));
        return "skills";
    }

    @GetMapping("/{skillId}")
    public String getSkill(@PathVariable String username,
                             @PathVariable int skillId,
                             Model model) {
        model.addAttribute("skill", skillService.getSkill(username, skillId));
        return "skill";
    }

    @GetMapping("/{skillId}/sign-up")
    public String signUp(@AuthenticationPrincipal UserDetails userDetails,
                     @PathVariable String username,
                     @PathVariable int skillId,
                          Model model) {
        model.addAttribute("success", skillService.signUp(username, skillId, userDetails));
        return "skill";
    }

    @GetMapping("/{skillId}/rate")
    public String getRates(@PathVariable String username,
                                  @PathVariable Long skillId,
                                  @RequestParam int pageNum,
                                  @RequestParam int pageSize,
                                  Model model) {
        model.addAttribute("rates", skillService.getRatesOfSkill(username, skillId, pageNum, pageSize));
        return "rates";
    }

    @PostMapping("/{skillId}/rate")
    public void rate(@AuthenticationPrincipal UserDetails userDetails,
                     @PathVariable String username,
                     @PathVariable Long skillId,
                     @RequestBody RateFormDto rateDto) {
        skillService.addRate(username, skillId, rateDto, userDetails.getUsername());
    }




}
