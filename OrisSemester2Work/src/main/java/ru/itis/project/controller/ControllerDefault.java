package ru.itis.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.project.dto.SkillCreateDto;

@Controller
@RequestMapping("/aboba")
public class ControllerDefault {

    @GetMapping
    public String aboba(Model model) {
        model.addAttribute("skill", new SkillCreateDto(null, null, null, null));
        return "addSkill";
    }

    @PostMapping
    public String aboba(@ModelAttribute SkillCreateDto skill) {
        return "addSkill";
    }
}
