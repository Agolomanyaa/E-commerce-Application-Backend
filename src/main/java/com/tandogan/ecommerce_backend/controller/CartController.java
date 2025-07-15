package com.tandogan.ecommerce_backend.controller;

import com.tandogan.ecommerce_backend.dto.request.AddToCartRequest;
import com.tandogan.ecommerce_backend.dto.request.UpdateCartItemRequest;
import com.tandogan.ecommerce_backend.dto.response.CartDto;
import com.tandogan.ecommerce_backend.model.User;
import com.tandogan.ecommerce_backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})

public class CartController {

    private final CartService cartService;

    // KULLANICININ SEPETİNİ GETİRİR
    @GetMapping
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal User user) {
        CartDto cartDto = cartService.getCartByUserId(user.getId());
        return ResponseEntity.ok(cartDto);
    }

    // SEPETE YENİ ÜRÜN EKLER
    @PostMapping
    public ResponseEntity<CartDto> addToCart(@AuthenticationPrincipal User user, @RequestBody AddToCartRequest request) {
        CartDto updatedCart = cartService.addToCart(user.getId(), request);
        return ResponseEntity.ok(updatedCart);
    }

    // SEPETTEKİ ÜRÜNÜN MİKTARINI GÜNCELLER
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartDto> updateCartItem(
            @AuthenticationPrincipal User user,
            @PathVariable Long cartItemId,
            @RequestBody UpdateCartItemRequest request) {
        CartDto updatedCart = cartService.updateCartItemQuantity(user.getId(), cartItemId, request);
        return ResponseEntity.ok(updatedCart);
    }

    // SEPETTEN ÜRÜN SİLER
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartDto> removeFromCart(@AuthenticationPrincipal User user, @PathVariable Long cartItemId) {
        CartDto updatedCart = cartService.removeFromCart(user.getId(), cartItemId);
        return ResponseEntity.ok(updatedCart);
    }
}