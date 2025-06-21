package ru.itis.project.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.itis.project.dto.*;
import ru.itis.project.exception.UserNotFoundException;
import ru.itis.project.service.CommentService;
import ru.itis.project.service.LessonService;
import ru.itis.project.service.ReviewService;
import ru.itis.project.service.SkillService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/{username}/skill")
@RequiredArgsConstructor
public class SkillRestController {

    private final SkillService skillService;
    private final LessonService lessonService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    @GetMapping
    public Page<SkillBasicDto> getSkills(@PathVariable String username,
                                         @RequestParam String query,
                                         @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return skillService.getSkillsByUsernameAndQuery(username, query, pageNum, pageSize);
    }

    @PostMapping
    public SkillDto save(@PathVariable String username, @RequestBody SkillCreateDto skillDto) {
        return null;
    }

    @GetMapping("/{skillId}/slots")
    public List<LessonSlotDto> getSlots(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable String username,
                                        @PathVariable Long skillId,
                                        @RequestParam int range) {
        LocalDateTime endTime = LocalDateTime.now().plusDays(range);
        return lessonService.getFreeTimes(userDetails.getUsername(), skillId, endTime);
    }

    @PostMapping("/{skillId}/sign-up")
    public void signUp(@AuthenticationPrincipal UserDetails userDetails,
                       @PathVariable String username,
                       @PathVariable Long skillId,
                       @RequestBody LessonSlotDto lessonSlotDto) {
        lessonService.signUp(userDetails.getUsername(), username, skillId, lessonSlotDto);
    }

    @PostMapping("/{skillId}/review")
    public RateDto rate(@AuthenticationPrincipal UserDetails userDetails,
                     @PathVariable String username,
                     @PathVariable Long skillId,
                     @RequestBody RateFormDto rateDto) {
        if (userDetails == null) {
            throw new UserNotFoundException("Not authenticated");
        }
        return reviewService.addRate(username, skillId, rateDto, userDetails.getUsername());
    }

    @PostMapping("/{skillId}/comment")
    public CommentDto addComment(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable String username,
                                 @PathVariable Long skillId,
                                 @RequestBody CommentFormDto commentDto) {
        if (userDetails == null) {
            throw new UserNotFoundException("Not authenticated");
        }
        return commentService.addComment(userDetails.getUsername(), commentDto, skillId);
    }

}
