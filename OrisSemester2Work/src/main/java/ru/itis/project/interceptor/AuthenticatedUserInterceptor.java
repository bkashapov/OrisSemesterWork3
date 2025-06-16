package ru.itis.project.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.project.dto.UserDto;
import ru.itis.project.entity.User;
import ru.itis.project.exception.UserNotFoundException;
import ru.itis.project.mapper.UserDtoMapper;
import ru.itis.project.repository.UserRepository;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticatedUserInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView == null) return;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            UserDto user = userDtoMapper.userToUserDto(userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException("Failed to load user in interceptor")));
            modelAndView.addObject("user", user);
        }
    }

}
