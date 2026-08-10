package com.gogreen.ai.dto.response;

import com.gogreen.ai.entity.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class NotificationResponseDto {

    private UUID id;
    private UUID userId;
    private NotificationType type;
    private String title;
    private String message;
    private boolean read;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
