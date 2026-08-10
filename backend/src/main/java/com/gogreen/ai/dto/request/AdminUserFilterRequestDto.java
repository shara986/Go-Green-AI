package com.gogreen.ai.dto.request;

import com.gogreen.ai.entity.enums.UserApprovalStatus;
import com.gogreen.ai.entity.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserFilterRequestDto {
    private String search;
    private UserRole role;
    private UserApprovalStatus approvalStatus;
    private Boolean enabled;
    private String sortBy = "createdAt";
    private String sortDir = "desc";
    private int page = 0;
    private int size = 20;
}
