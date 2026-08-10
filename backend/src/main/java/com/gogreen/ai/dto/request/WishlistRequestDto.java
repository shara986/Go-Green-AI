package com.gogreen.ai.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class WishlistRequestDto {

    @NotNull
    private UUID userId;
}
