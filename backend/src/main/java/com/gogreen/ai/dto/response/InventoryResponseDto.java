package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InventoryResponseDto {

    private UUID id;
    private UUID plantId;
    private String plantName;
    private Integer stockLevel;
    private Integer reservedQuantity;
    private Integer reorderLevel;
    private LocalDateTime lastRestockDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
