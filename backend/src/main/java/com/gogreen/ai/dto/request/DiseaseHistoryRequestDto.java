package com.gogreen.ai.dto.request;

import com.gogreen.ai.entity.enums.DiseaseSeverity;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DiseaseHistoryRequestDto {

    @NotNull
    private UUID userId;

    @NotBlank
    @Size(max = 150)
    private String plantName;

    @NotBlank
    @Size(max = 150)
    private String diseaseIdentified;

    @NotNull
    private LocalDate dateIdentified;

    private DiseaseSeverity severity;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private Double confidenceScore;

    @Size(max = 2000)
    private String recommendedAction;

    @Size(max = 500)
    private String imageUrl;
}
