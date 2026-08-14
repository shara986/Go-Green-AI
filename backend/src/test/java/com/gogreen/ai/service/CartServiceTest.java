package com.gogreen.ai.service;

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
import com.gogreen.ai.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void shouldGetCartForUser() {
        String username = "johndoe";
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUser(user);

        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setId(cart.getId());
        responseDto.setUserId(userId);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartMapper.toResponseDto(cart)).thenReturn(responseDto);

        CartResponseDto result = cartService.getCartForUser(username);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    void shouldAddItemToCartSuccessfully() {
        String username = "johndoe";
        UUID userId = UUID.randomUUID();
        UUID plantId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        Plant plant = new Plant();
        plant.setId(plantId);
        plant.setActive(true);
        plant.setStock(10);
        plant.setPrice(25.0);

        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setPlantId(plantId);
        requestDto.setQuantity(2);

        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setId(cart.getId());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));
        when(cartItemRepository.findByCartIdAndPlantId(cart.getId(), plantId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toResponseDto(cart)).thenReturn(responseDto);

        CartResponseDto result = cartService.addItemToCart(username, requestDto);

        assertNotNull(result);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void shouldThrowExceptionWhenStockInsufficient() {
        String username = "johndoe";
        UUID userId = UUID.randomUUID();
        UUID plantId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUser(user);

        Plant plant = new Plant();
        plant.setId(plantId);
        plant.setActive(true);
        plant.setStock(1);

        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setPlantId(plantId);
        requestDto.setQuantity(5);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(plantRepository.findById(plantId)).thenReturn(Optional.of(plant));

        APIException exception = assertThrows(APIException.class, () -> cartService.addItemToCart(username, requestDto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
