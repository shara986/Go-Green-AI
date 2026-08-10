package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.OrderItemRequestDto;
import com.gogreen.ai.dto.response.OrderItemResponseDto;
import com.gogreen.ai.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface OrderItemMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "plant.id", target = "plantId")
    @Mapping(source = "plant.name", target = "plantName")
    OrderItemResponseDto toResponseDto(OrderItem orderItem);

    List<OrderItemResponseDto> toResponseDtoList(List<OrderItem> orderItems);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "plant", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderItem toEntity(OrderItemRequestDto dto);
}
