package com.gogreen.ai.dto.response;

import com.gogreen.ai.entity.enums.PlantType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PlantResponseDto {

    private UUID id;
    private UUID nurseryId;
    private String nurseryName;
    private UUID categoryId;
    private String categoryName;
    private String name;
    private String scientificName;
    private String sku;
    private String description;
    private String careInstructions;
    private Double price;
    private Integer stock;
    private PlantType plantType;
    private String imageUrl;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
