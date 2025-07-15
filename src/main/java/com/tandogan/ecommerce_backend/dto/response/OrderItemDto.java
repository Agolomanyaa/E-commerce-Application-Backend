package com.tandogan.ecommerce_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productImageUrl;
    private String variantInfo; // Örn: "Kırmızı / 42"
    private int quantity;
    private BigDecimal price;
}