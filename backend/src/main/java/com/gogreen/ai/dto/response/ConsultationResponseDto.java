package com.gogreen.ai.dto.response;

import com.gogreen.ai.entity.enums.ConsultationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ConsultationResponseDto {

    private UUID id;
    private UUID userId;
    private String userName;
    private UUID expertId;
    private String expertName;
    private String subject;
    private ConsultationStatus status;
    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
    private String notes;
    private String meetingLink;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
