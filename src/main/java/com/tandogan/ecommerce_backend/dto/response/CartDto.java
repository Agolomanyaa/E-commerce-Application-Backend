package com.tandogan.ecommerce_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartDto {
    private Long id; // Sepetin ID'si
    private Long userId; // Kullanıcının ID'si
    private List<CartItemDto> cartItems;
    private BigDecimal totalPrice; // Sepetin genel toplamı
    private int totalItemCount; // Sepetteki toplam ürün adedi
}