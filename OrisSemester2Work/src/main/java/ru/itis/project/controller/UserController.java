package ru.itis.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.project.dto.UserPageDto;
import ru.itis.project.service.HomepageService;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final HomepageService homepageService;

    @GetMapping("/{username}")
    public String user(@AuthenticationPrincipal UserDetails userDetails,
                       @PathVariable String username,
                       Model model) {
        if (userDetails != null && userDetails.getUsername().equals(username)) {
            return "redirect:/me";
        }
        UserPageDto userPageDto = homepageService.getHomepage(username);
        model.addAttribute("pageUser", userPageDto.user());
        model.addAttribute("skills", userPageDto.skills());
        return "user";
    }
}
