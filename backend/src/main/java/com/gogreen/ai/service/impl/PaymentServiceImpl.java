package com.gogreen.ai.service.impl;

import com.gogreen.ai.dto.request.PaymentRequestDto;
import com.gogreen.ai.dto.response.PageResponseDto;
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
import com.gogreen.ai.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;

    private static final Random RANDOM = new Random();

    @Override
    public PaymentResponseDto processPayment(String username, PaymentRequestDto paymentRequestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found with username: " + username));

        Order order = orderRepository.findById(paymentRequestDto.getOrderId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Order not found with id: " + paymentRequestDto.getOrderId()));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new APIException(HttpStatus.FORBIDDEN, "You are not authorized to process payment for this order");
        }

        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new APIException(HttpStatus.CONFLICT, "A payment record already exists for this order");
        }

        Payment payment = paymentMapper.toEntity(paymentRequestDto);
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        
        String txnId = paymentRequestDto.getTransactionId();
        if (txnId == null || txnId.isBlank()) {
            txnId = generateTransactionId();
        }
        payment.setTransactionId(txnId);

        Payment savedPayment = paymentRepository.save(payment);

        if (savedPayment.getStatus() == PaymentStatus.COMPLETED) {
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
        } else if (savedPayment.getStatus() == PaymentStatus.FAILED) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }

        return paymentMapper.toResponseDto(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByOrderId(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Payment not found for order id: " + orderId));
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Payment not found with transaction id: " + transactionId));
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<PaymentResponseDto> getAllPayments(String search, PaymentStatus status, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Payment> payments = paymentRepository.searchPayments(search, status, pageable);

        PageResponseDto<PaymentResponseDto> pageResponse = new PageResponseDto<>();
        pageResponse.setContent(paymentMapper.toResponseDtoList(payments.getContent()));
        pageResponse.setPage(payments.getNumber());
        pageResponse.setSize(payments.getSize());
        pageResponse.setTotalElements(payments.getTotalElements());
        pageResponse.setTotalPages(payments.getTotalPages());

        return pageResponse;
    }

    private String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNum = 1000 + RANDOM.nextInt(9000);
        return "TXN-" + timestamp + "-" + randomNum;
    }
}
