package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AnnouncementResponseDto {
    private UUID id;
    private String title;
    private String message;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
