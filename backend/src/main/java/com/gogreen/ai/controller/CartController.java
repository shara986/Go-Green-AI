package com.gogreen.ai.controller;

import com.gogreen.ai.dto.ApiResponse;
import com.gogreen.ai.dto.request.CartItemRequestDto;
import com.gogreen.ai.dto.response.CartResponseDto;
import com.gogreen.ai.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart", description = "Customer Shopping Cart Management APIs")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get user shopping cart", description = "Retrieves current active shopping cart for the authenticated user.")
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart(Authentication authentication) {
        CartResponseDto cart = cartService.getCartForUser(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Cart fetched successfully", cart));
    }

    @PostMapping("/items")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add item to cart", description = "Adds a plant item to the shopping cart or increments quantity if existing.")
    public ResponseEntity<ApiResponse<CartResponseDto>> addItemToCart(
            Authentication authentication,
            @Valid @RequestBody CartItemRequestDto cartItemRequestDto) {
        CartResponseDto updatedCart = cartService.addItemToCart(authentication.getName(), cartItemRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Item added to cart successfully", updatedCart));
    }

    @PutMapping("/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update cart item quantity", description = "Updates quantity of a specific item in the shopping cart.")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateItemQuantity(
            Authentication authentication,
            @PathVariable UUID itemId,
            @RequestParam("quantity") int quantity) {
        CartResponseDto updatedCart = cartService.updateCartItemQuantity(authentication.getName(), itemId, quantity);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cart item updated successfully", updatedCart));
    }

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Remove item from cart", description = "Removes a specific item from the shopping cart.")
    public ResponseEntity<ApiResponse<CartResponseDto>> removeItem(
            Authentication authentication,
            @PathVariable UUID itemId) {
        CartResponseDto updatedCart = cartService.removeItemFromCart(authentication.getName(), itemId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Item removed from cart successfully", updatedCart));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Clear shopping cart", description = "Clears all items from the authenticated user's shopping cart.")
    public ResponseEntity<ApiResponse<String>> clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Cart cleared successfully", "Cart cleared"));
    }
}
