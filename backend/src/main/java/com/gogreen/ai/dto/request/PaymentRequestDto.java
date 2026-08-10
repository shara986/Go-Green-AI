package com.gogreen.ai.dto.request;

import com.gogreen.ai.entity.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PaymentRequestDto {

    @NotNull
    private UUID orderId;

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    private PaymentStatus status;

    @NotBlank
    @Size(max = 50)
    private String paymentMethod;

    @Size(max = 100)
    private String transactionId;
}
