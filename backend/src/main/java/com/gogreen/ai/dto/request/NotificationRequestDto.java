package com.gogreen.ai.dto.request;

import com.gogreen.ai.entity.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NotificationRequestDto {

    @NotNull
    private UUID userId;

    @NotNull
    private NotificationType type;

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String message;

    private boolean read;
}
