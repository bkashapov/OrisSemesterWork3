package ru.itis.project.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.itis.project.dto.SkillBasicDto;
import ru.itis.project.service.SkillService;

@RestController
@RequestMapping("/api/v1/user/{username}/skill")
@RequiredArgsConstructor
public class SkillRestController {

    private final SkillService skillService;

    @GetMapping
    public Page<SkillBasicDto> getSkills(@PathVariable String username,
                                         @RequestParam String query,
                                         @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return skillService.getSkillsByUsernameAndQuery(username, query, pageNum, pageSize);
    }


}
