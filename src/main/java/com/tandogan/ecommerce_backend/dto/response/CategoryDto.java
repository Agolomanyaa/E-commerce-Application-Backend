package com.tandogan.ecommerce_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private String gender;
}