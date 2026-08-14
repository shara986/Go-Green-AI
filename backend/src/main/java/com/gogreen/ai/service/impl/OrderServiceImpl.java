package com.gogreen.ai.service.impl;

import com.gogreen.ai.dto.request.OrderRequestDto;
import com.gogreen.ai.dto.response.OrderResponseDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.entity.*;
import com.gogreen.ai.entity.enums.OrderStatus;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.OrderMapper;
import com.gogreen.ai.repository.*;
import com.gogreen.ai.service.OrderService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    private static final Random RANDOM = new Random();

    @Override
    public OrderResponseDto createOrderFromCart(String username, OrderRequestDto orderRequestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found with username: " + username));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Shopping cart is empty"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Cannot place an order with an empty cart");
        }

        // Validate stock for all cart items first
        for (CartItem item : cart.getItems()) {
            Plant plant = item.getPlant();
            if (!plant.isActive()) {
                throw new APIException(HttpStatus.BAD_REQUEST, "Plant '" + plant.getName() + "' is currently inactive and cannot be ordered");
            }
            if (plant.getStock() < item.getQuantity()) {
                throw new APIException(HttpStatus.BAD_REQUEST, "Insufficient stock for plant '" + plant.getName() + "'. Available: " + plant.getStock());
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setBillingAddress(orderRequestDto.getBillingAddress() != null ? orderRequestDto.getBillingAddress() : orderRequestDto.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Plant plant = cartItem.getPlant();
            double subtotal = plant.getPrice() * cartItem.getQuantity();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPlant(plant);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(plant.getPrice());
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);
            totalAmount += subtotal;

            // Deduct stock
            plant.setStock(plant.getStock() - cartItem.getQuantity());
            plantRepository.save(plant);
        }

        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Order not found with id: " + id));
        return orderMapper.toResponseDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Order not found with order number: " + orderNumber));
        return orderMapper.toResponseDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found with username: " + username));
        List<Order> orders = orderRepository.findByUserId(user.getId());
        return orderMapper.toResponseDtoList(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<OrderResponseDto> getAllOrders(String search, OrderStatus status, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Order> orders = orderRepository.searchOrders(search, status, pageable);

        PageResponseDto<OrderResponseDto> pageResponse = new PageResponseDto<>();
        pageResponse.setContent(orderMapper.toResponseDtoList(orders.getContent()));
        pageResponse.setPage(orders.getNumber());
        pageResponse.setSize(orders.getSize());
        pageResponse.setTotalElements(orders.getTotalElements());
        pageResponse.setTotalPages(orders.getTotalPages());

        return pageResponse;
    }

    @Override
    public OrderResponseDto updateOrderStatus(UUID id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Order not found with id: " + id));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(updatedOrder);
    }

    @Override
    public OrderResponseDto cancelOrder(String username, UUID id) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found with username: " + username));

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Order not found with id: " + id));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new APIException(HttpStatus.FORBIDDEN, "You are not authorized to cancel this order");
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PROCESSING) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Order cannot be cancelled in its current status: " + order.getStatus());
        }

        // Restore stock
        for (OrderItem item : order.getItems()) {
            Plant plant = item.getPlant();
            plant.setStock(plant.getStock() + item.getQuantity());
            plantRepository.save(plant);
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(updatedOrder);
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomNum = 1000 + RANDOM.nextInt(9000);
        return "ORD-" + timestamp + "-" + randomNum;
    }
}
