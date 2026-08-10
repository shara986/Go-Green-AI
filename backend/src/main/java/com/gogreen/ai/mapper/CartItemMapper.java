package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.CartItemRequestDto;
import com.gogreen.ai.dto.response.CartItemResponseDto;
import com.gogreen.ai.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface CartItemMapper {

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "plant.id", target = "plantId")
    @Mapping(source = "plant.name", target = "plantName")
    @Mapping(source = "plant.price", target = "plantPrice")
    CartItemResponseDto toResponseDto(CartItem cartItem);

    List<CartItemResponseDto> toResponseDtoList(List<CartItem> cartItems);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "plant", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CartItem toEntity(CartItemRequestDto dto);
}
