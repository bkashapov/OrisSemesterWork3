package ru.itis.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class JwtTokenPairDto {

    private String accessToken;
    private String refreshToken;

}
