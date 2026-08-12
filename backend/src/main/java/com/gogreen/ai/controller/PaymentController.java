package com.gogreen.ai.controller;

import com.gogreen.ai.dto.ApiResponse;
import com.gogreen.ai.dto.request.PaymentRequestDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.dto.response.PaymentResponseDto;
import com.gogreen.ai.entity.enums.PaymentStatus;
import com.gogreen.ai.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment Gateway & Processing Management APIs")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Process payment for order", description = "Processes payment for a placed order and updates order status upon completion.")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> processPayment(
            Authentication authentication,
            @Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto payment = paymentService.processPayment(authentication.getName(), paymentRequestDto);
        return new ResponseEntity<>(new ApiResponse<>(true, "Payment processed successfully", payment), HttpStatus.CREATED);
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get payment by Order ID", description = "Retrieves payment record details for a specific order.")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentByOrderId(@PathVariable UUID orderId) {
        PaymentResponseDto payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment details retrieved successfully", payment));
    }

    @GetMapping("/txn/{transactionId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get payment by Transaction ID", description = "Retrieves payment record details by unique transaction ID.")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentByTransactionId(@PathVariable String transactionId) {
        PaymentResponseDto payment = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment details retrieved successfully", payment));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all payments", description = "Retrieves paginated list of all payment transactions (Admin only).")
    public ResponseEntity<ApiResponse<PageResponseDto<PaymentResponseDto>>> getAllPayments(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "status", required = false) PaymentStatus status,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        PageResponseDto<PaymentResponseDto> payments = paymentService.getAllPayments(search, status, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(new ApiResponse<>(true, "All payment transactions retrieved successfully", payments));
    }
}
