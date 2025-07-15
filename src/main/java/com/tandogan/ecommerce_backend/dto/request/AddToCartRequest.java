package com.tandogan.ecommerce_backend.dto.request;

import lombok.Data;

@Data
public class AddToCartRequest {
    // Hangi varyantın sepete ekleneceğini belirtmek için variantId kullanılır.
    private Long variantId;
    private int quantity;
}