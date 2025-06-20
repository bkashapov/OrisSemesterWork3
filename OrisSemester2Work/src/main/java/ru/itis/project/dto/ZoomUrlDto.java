package ru.itis.project.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ZoomUrlDto {

    private String startUrl;
    private String joinUrl;
}
