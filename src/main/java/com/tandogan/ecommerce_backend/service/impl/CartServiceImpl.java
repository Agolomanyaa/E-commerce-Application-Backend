package com.tandogan.ecommerce_backend.service.impl;

import com.tandogan.ecommerce_backend.dto.request.AddToCartRequest;
import com.tandogan.ecommerce_backend.dto.request.UpdateCartItemRequest;
import com.tandogan.ecommerce_backend.dto.response.CartDto;
import com.tandogan.ecommerce_backend.dto.response.CartItemDto;
import com.tandogan.ecommerce_backend.exception.ResourceNotFoundException;
import com.tandogan.ecommerce_backend.model.*;
import com.tandogan.ecommerce_backend.repository.*;
import com.tandogan.ecommerce_backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public CartDto addToCart(Long userId, AddToCartRequest request) {
        Cart cart = getOrCreateCartByUserId(userId);
        ProductVariant variant = productVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Variant not found with id: " + request.getVariantId()));

        if (variant.getStock() < request.getQuantity()) {
            throw new RuntimeException("Not enough stock for product: " + variant.getProduct().getName() + " (" + variant.getColor() + ", " + variant.getSize() + ")");
        }

        cartItemRepository.findByCartIdAndVariantId(cart.getId(), variant.getId())
                .ifPresentOrElse(
                        cartItem -> {
                            int newQuantity = cartItem.getQuantity() + request.getQuantity();
                            if (variant.getStock() < newQuantity) {
                                throw new RuntimeException("Not enough stock for product: " + variant.getProduct().getName());
                            }
                            cartItem.setQuantity(newQuantity);
                            cartItemRepository.save(cartItem);
                        },
                        () -> {
                            CartItem newCartItem = CartItem.builder()
                                    .cart(cart)
                                    .variant(variant)
                                    .quantity(request.getQuantity())
                                    .price(variant.getProduct().getPrice())
                                    .build();
                            cart.getCartItems().add(newCartItem);
                        }
                );

        Cart updatedCart = cartRepository.save(cart);
        return convertToCartDto(updatedCart);
    }

    @Override
    @Transactional
    public CartDto updateCartItemQuantity(Long userId, Long cartItemId, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCartByUserId(userId);
        CartItem cartItemToUpdate = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItemToUpdate.getCart().getId().equals(cart.getId())) {
            throw new SecurityException("User cannot update items in another user's cart.");
        }

        if (request.getQuantity() <= 0) {
            return removeFromCart(userId, cartItemId);
        }

        if (cartItemToUpdate.getVariant().getStock() < request.getQuantity()) {
            throw new RuntimeException("Not enough stock for product: " + cartItemToUpdate.getVariant().getProduct().getName());
        }

        cartItemToUpdate.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItemToUpdate);

        return convertToCartDto(cart);
    }

    private CartItemDto convertToCartItemDto(CartItem cartItem) {
        Product product = cartItem.getVariant().getProduct();
        return CartItemDto.builder()
                .id(cartItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .variantInfo(cartItem.getVariant().getColor() + " / " + cartItem.getVariant().getSize())
                // DÜZELTME: Set'ten güvenli bir şekilde ilk resmi alır.
                .productImageUrl(product.getImages().stream().findFirst().map(ProductImage::getUrl).orElse("no-image.jpg"))
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .lineTotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .build();
    }

    @Override
    public CartDto getCartByUserId(Long userId) {
        Cart cart = getOrCreateCartByUserId(userId);
        return convertToCartDto(cart);
    }

    @Override
    @Transactional
    public CartDto removeFromCart(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCartByUserId(userId);
        CartItem cartItemToRemove = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (!cartItemToRemove.getCart().getId().equals(cart.getId())) {
            throw new SecurityException("User cannot delete items from another user's cart.");
        }

        cart.getCartItems().remove(cartItemToRemove);
        cartItemRepository.delete(cartItemToRemove);

        return convertToCartDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public List<CartItem> getCartItemsByUserId(Long userId) {
        Cart cart = getOrCreateCartByUserId(userId);
        return cart.getCartItems();
    }

    private Cart getOrCreateCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });
    }

    private CartDto convertToCartDto(Cart cart) {
        List<CartItemDto> cartItemDtos = cart.getCartItems() != null ?
                cart.getCartItems().stream().map(this::convertToCartItemDto).collect(Collectors.toList()) :
                Collections.emptyList();

        BigDecimal totalPrice = cartItemDtos.stream()
                .map(CartItemDto::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItemCount = cartItemDtos.stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();

        return CartDto.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .cartItems(cartItemDtos)
                .totalPrice(totalPrice)
                .totalItemCount(totalItemCount)
                .build();
    }
}