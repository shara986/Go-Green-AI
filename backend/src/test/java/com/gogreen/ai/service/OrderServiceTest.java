package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.OrderRequestDto;
import com.gogreen.ai.dto.response.OrderResponseDto;
import com.gogreen.ai.entity.*;
import com.gogreen.ai.entity.enums.OrderStatus;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.OrderMapper;
import com.gogreen.ai.repository.*;
import com.gogreen.ai.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldCreateOrderFromCartSuccessfully() {
        String username = "johndoe";
        UUID userId = UUID.randomUUID();
        UUID plantId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        Plant plant = new Plant();
        plant.setId(plantId);
        plant.setName("Snake Plant");
        plant.setPrice(20.0);
        plant.setStock(10);
        plant.setActive(true);

        CartItem cartItem = new CartItem();
        cartItem.setPlant(plant);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUser(user);
        cart.setItems(new ArrayList<>(List.of(cartItem)));

        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setShippingAddress("123 Main St");
        requestDto.setBillingAddress("123 Main St");

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setOrderNumber("ORD-20260812-1000");
        order.setTotalAmount(40.0);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setOrderNumber("ORD-20260812-1000");
        responseDto.setTotalAmount(40.0);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(order)).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrderFromCart(username, requestDto);

        assertNotNull(result);
        assertEquals("ORD-20260812-1000", result.getOrderNumber());
        assertEquals(40.0, result.getTotalAmount());
        assertEquals(8, plant.getStock()); // Stock deducted from 10 to 8
    }

    @Test
    void shouldThrowBadRequestWhenCartIsEmpty() {
        String username = "johndoe";
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setShippingAddress("123 Main St");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        APIException exception = assertThrows(APIException.class, () -> orderService.createOrderFromCart(username, requestDto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void shouldCancelOrderAndRestoreStock() {
        String username = "johndoe";
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Plant plant = new Plant();
        plant.setStock(5);

        OrderItem orderItem = new OrderItem();
        orderItem.setPlant(plant);
        orderItem.setQuantity(3);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setItems(List.of(orderItem));

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setStatus(OrderStatus.CANCELLED);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponseDto(order)).thenReturn(responseDto);

        OrderResponseDto result = orderService.cancelOrder(username, orderId);

        assertNotNull(result);
        assertEquals(OrderStatus.CANCELLED, result.getStatus());
        assertEquals(8, plant.getStock()); // Stock restored from 5 to 8
    }
}
