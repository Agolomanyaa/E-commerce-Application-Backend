package com.tandogan.ecommerce_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private List<String> imageUrls;
    // HATA BURADAYDI: İsim 'ProductVariantRequest' olarak düzeltildi.
    private List<ProductVariantRequest> variants;
}