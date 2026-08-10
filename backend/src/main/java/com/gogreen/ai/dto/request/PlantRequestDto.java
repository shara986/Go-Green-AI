package com.gogreen.ai.dto.request;

import com.gogreen.ai.entity.enums.PlantType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlantRequestDto {

    @NotNull
    private UUID nurseryId;

    @NotNull
    private UUID categoryId;

    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 150)
    private String scientificName;

    @NotBlank
    @Size(max = 50)
    private String sku;

    @Size(max = 2000)
    private String description;

    @Size(max = 2000)
    private String careInstructions;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotNull
    private PlantType plantType;

    @Size(max = 500)
    private String imageUrl;

    private boolean active = true;
}
