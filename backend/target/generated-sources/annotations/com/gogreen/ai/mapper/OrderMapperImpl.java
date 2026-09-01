package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.OrderRequestDto;
import com.gogreen.ai.dto.response.OrderResponseDto;
import com.gogreen.ai.entity.Order;
import com.gogreen.ai.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-01T23:00:35+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto toResponseDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponseDto orderResponseDto = new OrderResponseDto();

        orderResponseDto.setUserId( orderUserId( order ) );
        orderResponseDto.setId( order.getId() );
        orderResponseDto.setOrderNumber( order.getOrderNumber() );
        orderResponseDto.setTotalAmount( order.getTotalAmount() );
        orderResponseDto.setStatus( order.getStatus() );
        orderResponseDto.setShippingAddress( order.getShippingAddress() );
        orderResponseDto.setBillingAddress( order.getBillingAddress() );
        orderResponseDto.setItems( orderItemMapper.toResponseDtoList( order.getItems() ) );
        orderResponseDto.setCreatedAt( order.getCreatedAt() );
        orderResponseDto.setUpdatedAt( order.getUpdatedAt() );

        return orderResponseDto;
    }

    @Override
    public List<OrderResponseDto> toResponseDtoList(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderResponseDto> list = new ArrayList<OrderResponseDto>( orders.size() );
        for ( Order order : orders ) {
            list.add( toResponseDto( order ) );
        }

        return list;
    }

    @Override
    public Order toEntity(OrderRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Order order = new Order();

        order.setOrderNumber( dto.getOrderNumber() );
        order.setTotalAmount( dto.getTotalAmount() );
        order.setStatus( dto.getStatus() );
        order.setShippingAddress( dto.getShippingAddress() );
        order.setBillingAddress( dto.getBillingAddress() );

        return order;
    }

    private UUID orderUserId(Order order) {
        if ( order == null ) {
            return null;
        }
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
