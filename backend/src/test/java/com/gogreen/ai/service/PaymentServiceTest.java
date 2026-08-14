package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.PaymentRequestDto;
import com.gogreen.ai.dto.response.PaymentResponseDto;
import com.gogreen.ai.entity.Order;
import com.gogreen.ai.entity.Payment;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.entity.enums.OrderStatus;
import com.gogreen.ai.entity.enums.PaymentStatus;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.PaymentMapper;
import com.gogreen.ai.repository.OrderRepository;
import com.gogreen.ai.repository.PaymentRepository;
import com.gogreen.ai.repository.UserRepository;
import com.gogreen.ai.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void shouldProcessPaymentSuccessfully() {
        String username = "johndoe";
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setTotalAmount(100.0);
        order.setStatus(OrderStatus.PENDING);

        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setOrderId(orderId);
        requestDto.setPaymentMethod("CREDIT_CARD");
        requestDto.setStatus(PaymentStatus.COMPLETED);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(100.0);
        payment.setPaymentMethod("CREDIT_CARD");
        payment.setStatus(PaymentStatus.COMPLETED);

        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setOrderId(orderId);
        responseDto.setStatus(PaymentStatus.COMPLETED);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.empty());
        when(paymentMapper.toEntity(requestDto)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toResponseDto(payment)).thenReturn(responseDto);

        PaymentResponseDto result = paymentService.processPayment(username, requestDto);

        assertNotNull(result);
        assertEquals(PaymentStatus.COMPLETED, result.getStatus());
        assertEquals(OrderStatus.CONFIRMED, order.getStatus()); // Order updated to CONFIRMED
    }

    @Test
    void shouldThrowConflictWhenPaymentAlreadyExists() {
        String username = "johndoe";
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);

        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setOrderId(orderId);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(new Payment()));

        APIException exception = assertThrows(APIException.class, () -> paymentService.processPayment(username, requestDto));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }
}
