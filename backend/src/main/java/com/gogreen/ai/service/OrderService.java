package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.OrderRequestDto;
import com.gogreen.ai.dto.response.OrderResponseDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.entity.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponseDto createOrderFromCart(String username, OrderRequestDto orderRequestDto);

    OrderResponseDto getOrderById(UUID id);

    OrderResponseDto getOrderByOrderNumber(String orderNumber);

    List<OrderResponseDto> getOrdersForUser(String username);

    PageResponseDto<OrderResponseDto> getAllOrders(String search, OrderStatus status, int pageNo, int pageSize, String sortBy, String sortDir);

    OrderResponseDto updateOrderStatus(UUID id, OrderStatus status);

    OrderResponseDto cancelOrder(String username, UUID id);
}
