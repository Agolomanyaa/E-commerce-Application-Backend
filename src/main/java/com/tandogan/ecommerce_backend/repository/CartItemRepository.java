package com.tandogan.ecommerce_backend.repository;

import com.tandogan.ecommerce_backend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Artık bir sepet öğesini ürün ID'siyle değil, varyant ID'siyle arayacağız.
    Optional<CartItem> findByCartIdAndVariantId(Long cartId, Long variantId);
}