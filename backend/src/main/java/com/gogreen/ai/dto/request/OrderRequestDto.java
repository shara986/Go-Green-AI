package com.gogreen.ai.dto.request;

import com.gogreen.ai.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderRequestDto {

    @NotNull
    private UUID userId;

    @NotBlank
    @Size(max = 30)
    private String orderNumber;

    @NotNull
    @PositiveOrZero
    private Double totalAmount;

    @NotNull
    private OrderStatus status;

    @NotBlank
    @Size(max = 500)
    private String shippingAddress;

    @Size(max = 500)
    private String billingAddress;
}
