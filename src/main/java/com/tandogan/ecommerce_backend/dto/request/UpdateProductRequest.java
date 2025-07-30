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

    // Artık bu satır hata vermeyecek çünkü VariantRequest.java dosyasını oluşturduk.
    private List<VariantRequest> variants;

    // Alanın adını "isActive" yerine "active" olarak güncelliyoruz.
    private boolean active;
}