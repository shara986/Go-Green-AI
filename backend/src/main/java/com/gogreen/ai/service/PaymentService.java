package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.PaymentRequestDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.dto.response.PaymentResponseDto;
import com.gogreen.ai.entity.enums.PaymentStatus;

import java.util.UUID;

public interface PaymentService {

    PaymentResponseDto processPayment(String username, PaymentRequestDto paymentRequestDto);

    PaymentResponseDto getPaymentByOrderId(UUID orderId);

    PaymentResponseDto getPaymentByTransactionId(String transactionId);

    PageResponseDto<PaymentResponseDto> getAllPayments(String search, PaymentStatus status, int pageNo, int pageSize, String sortBy, String sortDir);
}
