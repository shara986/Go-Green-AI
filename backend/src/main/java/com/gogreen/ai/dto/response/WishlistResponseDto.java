package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class WishlistResponseDto {

    private UUID id;
    private UUID userId;
    private List<WishlistItemResponseDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
