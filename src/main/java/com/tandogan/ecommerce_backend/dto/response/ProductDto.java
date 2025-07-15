package com.tandogan.ecommerce_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Double rating;
    private Integer sellCount;
    private boolean isActive;
    private Integer totalStock; // Toplam stok
    private CategoryDto category;
    private List<ProductImageDto> images;
    private List<ProductVariantDto> variants; // Varyant listesi
}