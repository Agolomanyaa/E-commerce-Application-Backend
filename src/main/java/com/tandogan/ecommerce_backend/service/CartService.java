package com.tandogan.ecommerce_backend.service;

import com.tandogan.ecommerce_backend.dto.request.AddToCartRequest;
import com.tandogan.ecommerce_backend.dto.request.UpdateCartItemRequest;
import com.tandogan.ecommerce_backend.dto.response.CartDto;
import com.tandogan.ecommerce_backend.model.CartItem;

import java.util.List;

public interface CartService {

    CartDto addToCart(Long userId, AddToCartRequest request);

    CartDto getCartByUserId(Long userId);

    CartDto removeFromCart(Long userId, Long cartItemId);

    CartDto updateCartItemQuantity(Long userId, Long cartItemId, UpdateCartItemRequest request);

    void clearCart(Long userId);

    /**
     * Sadece diğer servislerin kullanması için, bir kullanıcının sepetindeki
     * CartItem entity'lerinin listesini döndürür.
     * @param userId Kullanıcı ID'si.
     * @return CartItem listesi.
     */
    List<CartItem> getCartItemsByUserId(Long userId); // YENİ EKLENEN METOT
}