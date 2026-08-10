package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OrderItemResponseDto {

    private UUID id;
    private UUID orderId;
    private UUID plantId;
    private String plantName;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
