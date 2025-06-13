package ru.itis.project.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.project.dto.RegistrationFormDto;
import ru.itis.project.entity.User;
import ru.itis.project.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void registerUser(RegistrationFormDto registrationFormDto){
        User user = new User()
                .setUsername(registrationFormDto.getUsername())
                .setHashedPassword(passwordEncoder.encode(registrationFormDto.getPassword()))
                .setEmail(registrationFormDto.getEmail())
                .setAvgRating(0.0)
                .setSkillPoints(5)
                .setDescription(registrationFormDto.getDescription());

        userRepository.save(user);
        log.info("User registered successfully");
    }

}
