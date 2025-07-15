package com.tandogan.ecommerce_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private Long id;
    private Long userId;
    private List<OrderItemDto> orderItems;
    private String shippingAddress;
    private LocalDateTime orderDate;
    private BigDecimal totalPrice;
    private String status;
}