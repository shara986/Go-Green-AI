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
    date = "2026-08-12T14:58:59+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
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
        orderResponseDto.setBillingAddress( order.getBillingAddress() );
        orderResponseDto.setCreatedAt( order.getCreatedAt() );
        orderResponseDto.setId( order.getId() );
        orderResponseDto.setItems( orderItemMapper.toResponseDtoList( order.getItems() ) );
        orderResponseDto.setOrderNumber( order.getOrderNumber() );
        orderResponseDto.setShippingAddress( order.getShippingAddress() );
        orderResponseDto.setStatus( order.getStatus() );
        orderResponseDto.setTotalAmount( order.getTotalAmount() );
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

        order.setBillingAddress( dto.getBillingAddress() );
        order.setOrderNumber( dto.getOrderNumber() );
        order.setShippingAddress( dto.getShippingAddress() );
        order.setStatus( dto.getStatus() );
        order.setTotalAmount( dto.getTotalAmount() );

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
