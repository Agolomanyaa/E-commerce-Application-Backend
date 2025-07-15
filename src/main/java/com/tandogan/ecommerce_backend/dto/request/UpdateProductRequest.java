package com.tandogan.ecommerce_backend.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private List<String> imageUrls;
    // HATA BURADAYDI: İsim 'ProductVariantRequest' olarak düzeltildi.
    private List<ProductVariantRequest> variants;
    private boolean isActive;
}