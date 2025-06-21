package ru.itis.project.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.project.dto.CommentClientDto;
import ru.itis.project.dto.CommentClientFormDto;
import ru.itis.project.wrapper.PageCommentDtoWrapper;

@Service
@RequiredArgsConstructor
public class CommentClient {

    @Value("${comment-service.host}")
    private String host;

    @Value("${comment-service.port}")
    private int port;

    private final RestTemplate restTemplate;

    public Page<CommentClientDto> getCommentsBySkillId(Long skillId, int page) {
        String url = String.format("http://%s:%d/public/skill/%d/comment?page=%d", host, port, skillId, page);
        ResponseEntity<PageCommentDtoWrapper> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {},
                skillId, page
        );
        return response.getBody().toPage();
    }

    public int getCommentCount(Long skillId) {
        String url = String.format("http://%s:%d/public/skill/%d/comment/count", host, port, skillId);
        ResponseEntity<Integer> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }

    public CommentClientDto addComment(CommentClientFormDto commentClientFormDto, Long skillId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(TokenClient.getToken()); // если требуется авторизация через JWT

        HttpEntity<CommentClientFormDto> request = new HttpEntity<>(commentClientFormDto, headers);


        String url = String.format("http://%s:%d/skill/%d/comment", host, port, skillId);

        ResponseEntity<CommentClientDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                CommentClientDto.class,
                skillId
        );

        return response.getBody();
    }
}
