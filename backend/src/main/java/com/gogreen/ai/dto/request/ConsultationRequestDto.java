package com.gogreen.ai.dto.request;

import com.gogreen.ai.entity.enums.ConsultationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ConsultationRequestDto {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID expertId;

    @NotBlank
    @Size(max = 200)
    private String subject;

    @NotNull
    private ConsultationStatus status;

    private LocalDateTime scheduledAt;

    private Integer durationMinutes;

    @Size(max = 1000)
    private String notes;

    @Size(max = 500)
    private String meetingLink;
}
