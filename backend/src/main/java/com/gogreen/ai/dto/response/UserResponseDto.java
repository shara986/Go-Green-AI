package com.gogreen.ai.dto.response;

import com.gogreen.ai.entity.enums.UserApprovalStatus;
import com.gogreen.ai.entity.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDto {

    private UUID id;
    private String name;
    private String username;
    private String email;
    private String phoneNumber;
    private boolean enabled;
    private UserApprovalStatus approvalStatus;
    private boolean deleted;
    private Set<UserRole> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
