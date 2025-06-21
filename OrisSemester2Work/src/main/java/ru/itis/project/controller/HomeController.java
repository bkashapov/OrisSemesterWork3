package ru.itis.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.project.service.SkillService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final SkillService skillService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("skills", skillService.getSkills());
        return "home";
    }
}
