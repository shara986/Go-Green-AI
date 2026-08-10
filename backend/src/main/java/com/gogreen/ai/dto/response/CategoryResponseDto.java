package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CategoryResponseDto {

    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String icon;
    private String imageUrl;
    private boolean active;
    private UUID parentCategoryId;
    private String parentCategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
