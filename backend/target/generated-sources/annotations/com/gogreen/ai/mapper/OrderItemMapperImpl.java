package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.OrderItemRequestDto;
import com.gogreen.ai.dto.response.OrderItemResponseDto;
import com.gogreen.ai.entity.Order;
import com.gogreen.ai.entity.OrderItem;
import com.gogreen.ai.entity.Plant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-15T21:49:07+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.1 (Oracle Corporation)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItemResponseDto toResponseDto(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();

        orderItemResponseDto.setOrderId( orderItemOrderId( orderItem ) );
        orderItemResponseDto.setPlantId( orderItemPlantId( orderItem ) );
        orderItemResponseDto.setPlantName( orderItemPlantName( orderItem ) );
        orderItemResponseDto.setId( orderItem.getId() );
        orderItemResponseDto.setQuantity( orderItem.getQuantity() );
        orderItemResponseDto.setUnitPrice( orderItem.getUnitPrice() );
        orderItemResponseDto.setSubtotal( orderItem.getSubtotal() );
        orderItemResponseDto.setCreatedAt( orderItem.getCreatedAt() );
        orderItemResponseDto.setUpdatedAt( orderItem.getUpdatedAt() );

        return orderItemResponseDto;
    }

    @Override
    public List<OrderItemResponseDto> toResponseDtoList(List<OrderItem> orderItems) {
        if ( orderItems == null ) {
            return null;
        }

        List<OrderItemResponseDto> list = new ArrayList<OrderItemResponseDto>( orderItems.size() );
        for ( OrderItem orderItem : orderItems ) {
            list.add( toResponseDto( orderItem ) );
        }

        return list;
    }

    @Override
    public OrderItem toEntity(OrderItemRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity( dto.getQuantity() );
        orderItem.setUnitPrice( dto.getUnitPrice() );
        orderItem.setSubtotal( dto.getSubtotal() );

        return orderItem;
    }

    private UUID orderItemOrderId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Order order = orderItem.getOrder();
        if ( order == null ) {
            return null;
        }
        UUID id = order.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID orderItemPlantId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Plant plant = orderItem.getPlant();
        if ( plant == null ) {
            return null;
        }
        UUID id = plant.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String orderItemPlantName(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        Plant plant = orderItem.getPlant();
        if ( plant == null ) {
            return null;
        }
        String name = plant.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
