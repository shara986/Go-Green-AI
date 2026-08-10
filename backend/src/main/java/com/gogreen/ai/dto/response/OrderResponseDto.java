package com.gogreen.ai.dto.response;

import com.gogreen.ai.entity.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderResponseDto {

    private UUID id;
    private UUID userId;
    private String orderNumber;
    private Double totalAmount;
    private OrderStatus status;
    private String shippingAddress;
    private String billingAddress;
    private List<OrderItemResponseDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
