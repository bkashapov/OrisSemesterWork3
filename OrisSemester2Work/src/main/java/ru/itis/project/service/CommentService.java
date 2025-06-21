package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.itis.project.client.CommentClient;
import ru.itis.project.dto.CommentClientDto;
import ru.itis.project.dto.CommentDto;
import ru.itis.project.dto.CommentClientFormDto;
import ru.itis.project.dto.CommentFormDto;
import ru.itis.project.entity.User;
import ru.itis.project.exception.BadRequestException;
import ru.itis.project.exception.UserNotFoundException;
import ru.itis.project.mapper.CommentDtoMapper;
import ru.itis.project.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentClient commentClient;
    private final CommentDtoMapper commentDtoMapper;
    private final UserRepository userRepository;

    public Page<CommentDto> getCommentsBySkillId(Long skillId, int page) {
        Page<CommentClientDto> comments = commentClient.getCommentsBySkillId(skillId, page);
        Map<Long, String> users = userRepository
                .findAllById(
                        comments
                                .stream()
                                .map(CommentClientDto::getAuthorId)
                                .filter(Objects::nonNull).toList()).stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));
        return comments.map(c -> commentDtoMapper.toCommentDto(c, users.get(c.getAuthorId())));
    }

    public int getCount(Long skillId) {
        return commentClient.getCommentCount(skillId);
    }

    public CommentDto addComment(String username, CommentFormDto commentFormDto, Long skillId) {
        User authUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        CommentClientFormDto commentClientFormDto = commentDtoMapper.toCommentClientFormDto(commentFormDto, authUser.getId());

        CommentClientDto commentDto = commentClient.addComment(commentClientFormDto, skillId);
        return commentDtoMapper.toCommentDto(commentDto, authUser.getUsername());
    }
}
