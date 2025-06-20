package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.itis.project.dto.SkillBasicDto;
import ru.itis.project.dto.UserPageDto;
import ru.itis.project.entity.Skill;
import ru.itis.project.entity.User;
import ru.itis.project.exception.UserNotFoundException;
import ru.itis.project.mapper.SkillDtoMapper;
import ru.itis.project.mapper.UserDtoMapper;
import ru.itis.project.repository.SkillRepository;
import ru.itis.project.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomepageService {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final SkillDtoMapper skillDtoMapper;
    private final UserDtoMapper userDtoMapper;

    public UserPageDto getHomepage(String username) {
        User user = userRepository.
                findByUsername(username).
                orElseThrow(() -> new UserNotFoundException("user not found: " + username));

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "rating"));
        List<SkillBasicDto> skills = skillRepository.findAllByUsername(user.getUsername(), pageable).stream().map(skillDtoMapper::toSkillBasicDto).toList();
        return new UserPageDto(
                userDtoMapper.userToUserDto(user),
                skills
        );
    }
}
