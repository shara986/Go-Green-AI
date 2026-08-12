package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.CartRequestDto;
import com.gogreen.ai.dto.response.CartResponseDto;
import com.gogreen.ai.entity.Cart;
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
public class CartMapperImpl implements CartMapper {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    public CartResponseDto toResponseDto(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartResponseDto cartResponseDto = new CartResponseDto();

        cartResponseDto.setUserId( cartUserId( cart ) );
        cartResponseDto.setCreatedAt( cart.getCreatedAt() );
        cartResponseDto.setId( cart.getId() );
        cartResponseDto.setItems( cartItemMapper.toResponseDtoList( cart.getItems() ) );
        cartResponseDto.setUpdatedAt( cart.getUpdatedAt() );

        return cartResponseDto;
    }

    @Override
    public List<CartResponseDto> toResponseDtoList(List<Cart> carts) {
        if ( carts == null ) {
            return null;
        }

        List<CartResponseDto> list = new ArrayList<CartResponseDto>( carts.size() );
        for ( Cart cart : carts ) {
            list.add( toResponseDto( cart ) );
        }

        return list;
    }

    @Override
    public Cart toEntity(CartRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Cart cart = new Cart();

        return cart;
    }

    private UUID cartUserId(Cart cart) {
        if ( cart == null ) {
            return null;
        }
        User user = cart.getUser();
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
