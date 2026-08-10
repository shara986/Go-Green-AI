package com.gogreen.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class PlantDiaryRequestDto {

    @NotNull
    private UUID userId;

    private UUID plantId;

    @NotBlank
    @Size(max = 150)
    private String plantName;

    @Size(max = 500)
    private String description;

    @NotNull
    private LocalDate dateStarted;
}
