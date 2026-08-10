package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlantDiaryResponseDto {

    private UUID id;
    private UUID userId;
    private UUID plantId;
    private String plantName;
    private String description;
    private LocalDate dateStarted;
    private List<PlantDiaryEntryResponseDto> entries;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
