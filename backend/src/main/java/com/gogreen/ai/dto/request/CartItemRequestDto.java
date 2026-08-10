package com.gogreen.ai.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartItemRequestDto {

    @NotNull
    private UUID cartId;

    @NotNull
    private UUID plantId;

    @NotNull
    @Positive
    private Integer quantity;
}
