package com.tandogan.ecommerce_backend.dto.request;

import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String name;
    private String gender;
    private Long parentId;
}