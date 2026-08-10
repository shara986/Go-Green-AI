package com.gogreen.ai.dto.response;

import com.gogreen.ai.entity.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RoleResponseDto {

    private UUID id;
    private UserRole name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
