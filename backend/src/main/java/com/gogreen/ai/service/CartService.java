package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.CartItemRequestDto;
import com.gogreen.ai.dto.response.CartResponseDto;

import java.util.UUID;

public interface CartService {

    CartResponseDto getCartForUser(String username);

    CartResponseDto addItemToCart(String username, CartItemRequestDto cartItemRequestDto);

    CartResponseDto updateCartItemQuantity(String username, UUID itemId, int quantity);

    CartResponseDto removeItemFromCart(String username, UUID itemId);

    void clearCart(String username);
}
