package ru.itis.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.project.client.CommentClient;
import ru.itis.project.dto.RateFormDto;
import ru.itis.project.service.CommentService;
import ru.itis.project.service.ReviewService;
import ru.itis.project.service.SkillService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/{username}/skill")
public class SkillController {

    private final SkillService skillService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    @GetMapping
    public String getSkills(@PathVariable String username,
                                    Model model) {
        model.addAttribute("skillUsername", username);
        return "skills";
    }

    @GetMapping("/{skillId}")
    public String getSkill(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable String username,
                             @PathVariable Long skillId,
                             Model model) {
        if (userDetails != null && userDetails.getUsername().equals(username)) {
            return "redirect:/me/skill/" + skillId;
        }
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

    @GetMapping("/{skillId}/review")
    public String getRates(@PathVariable String username,
                                  @PathVariable Long skillId,
                                  @RequestParam(required = false, defaultValue = "0") int pageNum,
                                  @RequestParam(required = false, defaultValue = "5") int pageSize,
                                  Model model) {
        model.addAttribute("skill", skillService.getSkill(username, skillId));
        model.addAttribute("rates", reviewService.getRatesOfSkill(username, skillId, pageNum, pageSize));
        return "rates";
    }

    @PostMapping("/{skillId}/review")
    public void rate(@AuthenticationPrincipal UserDetails userDetails,
                     @PathVariable String username,
                     @PathVariable Long skillId,
                     @RequestBody RateFormDto rateDto) {

        reviewService.addRate(username, skillId, rateDto, userDetails.getUsername());
    }

    @GetMapping("/{skillId}/comment")
    public String getComments(@PathVariable String username,
                                        @PathVariable Long skillId,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                              Model model) {
        model.addAttribute("skill", skillService.getSkill(username, skillId));
        model.addAttribute("comments", commentService.getCommentsBySkillId(skillId, page));
        model.addAttribute("baseUrl", String.format("/user/%s/skill/%d/comment", username, skillId));
        return "comment";
    }


}
