package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.CartRequestDto;
import com.gogreen.ai.dto.response.CartResponseDto;
import com.gogreen.ai.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class, uses = CartItemMapper.class)
public interface CartMapper {

    @Mapping(source = "user.id", target = "userId")
    CartResponseDto toResponseDto(Cart cart);

    List<CartResponseDto> toResponseDtoList(List<Cart> carts);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Cart toEntity(CartRequestDto dto);
}
