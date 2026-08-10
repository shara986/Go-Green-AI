package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class WishlistItemResponseDto {

    private UUID id;
    private UUID wishlistId;
    private UUID plantId;
    private String plantName;
    private Double plantPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
