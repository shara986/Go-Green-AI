package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CartResponseDto {

    private UUID id;
    private UUID userId;
    private List<CartItemResponseDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
