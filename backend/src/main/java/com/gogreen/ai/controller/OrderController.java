package com.gogreen.ai.controller;

import com.gogreen.ai.dto.ApiResponse;
import com.gogreen.ai.dto.request.OrderRequestDto;
import com.gogreen.ai.dto.response.OrderResponseDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.entity.enums.OrderStatus;
import com.gogreen.ai.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order Creation & Fulfillment Management APIs")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Checkout active cart", description = "Converts items in the current active cart into a placed order.")
    public ResponseEntity<ApiResponse<OrderResponseDto>> checkout(
            Authentication authentication,
            @Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto order = orderService.createOrderFromCart(authentication.getName(), orderRequestDto);
        return new ResponseEntity<>(new ApiResponse<>(true, "Order placed successfully", order), HttpStatus.CREATED);
    }

    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get user order history", description = "Retrieves order history for the authenticated user.")
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getMyOrders(Authentication authentication) {
        List<OrderResponseDto> orders = orderService.getOrdersForUser(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "User orders retrieved successfully", orders));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get order by ID", description = "Retrieves order details by unique order ID.")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(@PathVariable UUID id) {
        OrderResponseDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order retrieved successfully", order));
    }

    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get order by order number", description = "Retrieves order details by human-readable order number.")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderByOrderNumber(@PathVariable String orderNumber) {
        OrderResponseDto order = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order retrieved successfully", order));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders", description = "Retrieves paginated list of all customer orders (Admin only).")
    public ResponseEntity<ApiResponse<PageResponseDto<OrderResponseDto>>> getAllOrders(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        PageResponseDto<OrderResponseDto> orders = orderService.getAllOrders(search, status, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(new ApiResponse<>(true, "All orders retrieved successfully", orders));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status", description = "Updates status of an order (Admin only).")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam("status") OrderStatus status) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order status updated successfully", updatedOrder));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Cancel an order", description = "Cancels a pending order and restores plant inventory.")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(
            Authentication authentication,
            @PathVariable UUID id) {
        OrderResponseDto cancelledOrder = orderService.cancelOrder(authentication.getName(), id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order cancelled successfully", cancelledOrder));
    }
}
