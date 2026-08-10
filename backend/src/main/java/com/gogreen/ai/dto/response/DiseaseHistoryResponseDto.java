package com.gogreen.ai.dto.response;

import com.gogreen.ai.entity.enums.DiseaseSeverity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class DiseaseHistoryResponseDto {

    private UUID id;
    private UUID userId;
    private String plantName;
    private String diseaseIdentified;
    private LocalDate dateIdentified;
    private DiseaseSeverity severity;
    private Double confidenceScore;
    private String recommendedAction;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
