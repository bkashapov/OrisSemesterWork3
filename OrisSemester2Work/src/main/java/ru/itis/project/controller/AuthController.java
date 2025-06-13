package ru.itis.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.project.dto.LoginDto;
import ru.itis.project.dto.RegistrationFormDto;
import ru.itis.project.security.service.AuthService;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("registerForm", new RegistrationFormDto());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute @Valid RegistrationFormDto registrationFormDto,
                           Model model) {
        authService.registerUser(registrationFormDto);
        return "redirect:/api/v1/login";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("loginForm", new LoginDto());
        return "login";
    }
}
