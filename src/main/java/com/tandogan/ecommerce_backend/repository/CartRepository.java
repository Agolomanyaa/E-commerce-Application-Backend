package com.tandogan.ecommerce_backend.repository;

import com.tandogan.ecommerce_backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Kullanıcı ID'sine göre sepeti bulmak için özel metot.
    // Her kullanıcının sadece bir sepeti olacağı için Optional<Cart> döner.
    Optional<Cart> findByUserId(Long userId);
}