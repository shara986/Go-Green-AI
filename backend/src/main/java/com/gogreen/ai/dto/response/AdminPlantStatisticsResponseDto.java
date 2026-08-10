package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPlantStatisticsResponseDto {
    private long totalPlants;
    private long activePlants;
    private long inactivePlants;
    private long plantsByCategory;
}
