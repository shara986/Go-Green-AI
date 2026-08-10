package com.gogreen.ai.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InventoryRequestDto {

    @NotNull
    private UUID plantId;

    @NotNull
    @Min(0)
    private Integer stockLevel;

    @Min(0)
    private Integer reservedQuantity;

    @Min(0)
    private Integer reorderLevel;

    private LocalDateTime lastRestockDate;
}
