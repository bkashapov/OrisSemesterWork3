package ru.itis.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.project.dto.AuthDto;

@Controller
@RequestMapping("/api/v1/user")
public class AuthController {

    @GetMapping()
    public String getLogin() {
        return "login";
    }

    @PostMapping()
    public String postLogin(@RequestBody AuthDto authDto) {
        return "";
    }
}
