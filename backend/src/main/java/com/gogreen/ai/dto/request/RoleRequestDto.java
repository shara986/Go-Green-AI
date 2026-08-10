package com.gogreen.ai.dto.request;

import com.gogreen.ai.entity.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequestDto {

    @NotNull
    private UserRole name;
}
