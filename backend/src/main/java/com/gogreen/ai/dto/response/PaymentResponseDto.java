package com.gogreen.ai.dto.response;

import com.gogreen.ai.entity.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PaymentResponseDto {

    private UUID id;
    private UUID orderId;
    private String orderNumber;
    private Double amount;
    private PaymentStatus status;
    private String paymentMethod;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
