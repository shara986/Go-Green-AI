package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CartItemResponseDto {

    private UUID id;
    private UUID cartId;
    private UUID plantId;
    private String plantName;
    private Integer quantity;
    private Double plantPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
