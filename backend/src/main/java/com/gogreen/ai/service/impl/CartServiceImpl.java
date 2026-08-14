package com.gogreen.ai.service.impl;

import com.gogreen.ai.dto.request.CartItemRequestDto;
import com.gogreen.ai.dto.response.CartResponseDto;
import com.gogreen.ai.entity.Cart;
import com.gogreen.ai.entity.CartItem;
import com.gogreen.ai.entity.Plant;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.CartMapper;
import com.gogreen.ai.repository.CartItemRepository;
import com.gogreen.ai.repository.CartRepository;
import com.gogreen.ai.repository.PlantRepository;
import com.gogreen.ai.repository.UserRepository;
import com.gogreen.ai.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    public CartResponseDto getCartForUser(String username) {
        User user = getUserByUsername(username);
        Cart cart = getOrCreateCartForUser(user);
        return cartMapper.toResponseDto(cart);
    }

    @Override
    public CartResponseDto addItemToCart(String username, CartItemRequestDto cartItemRequestDto) {
        User user = getUserByUsername(username);
        Cart cart = getOrCreateCartForUser(user);

        Plant plant = plantRepository.findById(cartItemRequestDto.getPlantId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Plant not found with id: " + cartItemRequestDto.getPlantId()));

        if (!plant.isActive()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Plant is currently unavailable");
        }

        if (plant.getStock() < cartItemRequestDto.getQuantity()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Requested quantity exceeds available stock (" + plant.getStock() + ")");
        }

        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndPlantId(cart.getId(), plant.getId());

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + cartItemRequestDto.getQuantity();
            if (plant.getStock() < newQuantity) {
                throw new APIException(HttpStatus.BAD_REQUEST, "Total quantity in cart would exceed available stock (" + plant.getStock() + ")");
            }
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setPlant(plant);
            cartItem.setQuantity(cartItemRequestDto.getQuantity());
            cartItemRepository.save(cartItem);
            cart.getItems().add(cartItem);
        }

        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toResponseDto(updatedCart);
    }

    @Override
    public CartResponseDto updateCartItemQuantity(String username, UUID itemId, int quantity) {
        User user = getUserByUsername(username);
        Cart cart = getOrCreateCartForUser(user);

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Cart item not found with id: " + itemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new APIException(HttpStatus.FORBIDDEN, "Cart item does not belong to your cart");
        }

        if (quantity <= 0) {
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            Plant plant = cartItem.getPlant();
            if (plant.getStock() < quantity) {
                throw new APIException(HttpStatus.BAD_REQUEST, "Requested quantity exceeds available stock (" + plant.getStock() + ")");
            }
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toResponseDto(updatedCart);
    }

    @Override
    public CartResponseDto removeItemFromCart(String username, UUID itemId) {
        User user = getUserByUsername(username);
        Cart cart = getOrCreateCartForUser(user);

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Cart item not found with id: " + itemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new APIException(HttpStatus.FORBIDDEN, "Cart item does not belong to your cart");
        }

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toResponseDto(updatedCart);
    }

    @Override
    public void clearCart(String username) {
        User user = getUserByUsername(username);
        Cart cart = getOrCreateCartForUser(user);

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found with username: " + username));
    }

    private Cart getOrCreateCartForUser(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }
}
