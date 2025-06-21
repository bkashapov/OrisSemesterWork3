package ru.itis.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.itis.project.dto.ZoomUrlDto;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ZoomService {

    @Value("${zoom.client-id}")
    private String clientId;

    @Value("${zoom.client-secret}")
    private String clientSecret;

    @Value("${zoom.account-id}")
    private String accountId;

    private final RestTemplate restTemplate;

    public ZoomUrlDto createInstantMeeting(String topic) {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> settings = new HashMap<>();
        settings.put("host_video", true);
        settings.put("participant_video", true);
        settings.put("join_before_host", true);
        settings.put("enforce_login", false);
        settings.put("approval_type", 0);
        settings.put("waiting_room", false);

        Map<String, Object> body = new HashMap<>();
        body.put("topic", topic);
        body.put("type", 1);
        body.put("settings", settings);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.zoom.us/v2/users/me/meetings",
                entity,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Zoom service error: {}", response.getBody());
            throw new RuntimeException("Failed to create Zoom meeting: " + response.getStatusCode());
        }

        log.info("Zoom meeting created");
        return new ZoomUrlDto().setStartUrl((String) response.getBody().get("start_url"))
                .setJoinUrl((String) response.getBody().get("join_url"));
    }

    private String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "account_credentials");
        body.add("account_id", accountId);

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://zoom.us/oauth/token", request, Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to get Zoom access token: " + response.getStatusCode());
        }

        return (String) response.getBody().get("access_token");
    }
}
