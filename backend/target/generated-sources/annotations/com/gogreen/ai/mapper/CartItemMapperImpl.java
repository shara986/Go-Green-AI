package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.CartItemRequestDto;
import com.gogreen.ai.dto.response.CartItemResponseDto;
import com.gogreen.ai.entity.Cart;
import com.gogreen.ai.entity.CartItem;
import com.gogreen.ai.entity.Plant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-01T23:00:35+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class CartItemMapperImpl implements CartItemMapper {

    @Override
    public CartItemResponseDto toResponseDto(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();

        cartItemResponseDto.setCartId( cartItemCartId( cartItem ) );
        cartItemResponseDto.setPlantId( cartItemPlantId( cartItem ) );
        cartItemResponseDto.setPlantName( cartItemPlantName( cartItem ) );
        cartItemResponseDto.setPlantPrice( cartItemPlantPrice( cartItem ) );
        cartItemResponseDto.setId( cartItem.getId() );
        cartItemResponseDto.setQuantity( cartItem.getQuantity() );
        cartItemResponseDto.setCreatedAt( cartItem.getCreatedAt() );
        cartItemResponseDto.setUpdatedAt( cartItem.getUpdatedAt() );

        return cartItemResponseDto;
    }

    @Override
    public List<CartItemResponseDto> toResponseDtoList(List<CartItem> cartItems) {
        if ( cartItems == null ) {
            return null;
        }

        List<CartItemResponseDto> list = new ArrayList<CartItemResponseDto>( cartItems.size() );
        for ( CartItem cartItem : cartItems ) {
            list.add( toResponseDto( cartItem ) );
        }

        return list;
    }

    @Override
    public CartItem toEntity(CartItemRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        CartItem cartItem = new CartItem();

        cartItem.setQuantity( dto.getQuantity() );

        return cartItem;
    }

    private UUID cartItemCartId(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Cart cart = cartItem.getCart();
        if ( cart == null ) {
            return null;
        }
        UUID id = cart.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID cartItemPlantId(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Plant plant = cartItem.getPlant();
        if ( plant == null ) {
            return null;
        }
        UUID id = plant.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String cartItemPlantName(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Plant plant = cartItem.getPlant();
        if ( plant == null ) {
            return null;
        }
        String name = plant.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Double cartItemPlantPrice(CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }
        Plant plant = cartItem.getPlant();
        if ( plant == null ) {
            return null;
        }
        Double price = plant.getPrice();
        if ( price == null ) {
            return null;
        }
        return price;
    }
}
