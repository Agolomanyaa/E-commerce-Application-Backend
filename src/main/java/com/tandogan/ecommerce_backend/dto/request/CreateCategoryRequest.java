package com.tandogan.ecommerce_backend.dto.request;

import lombok.Data;

@Data
public class CreateCategoryRequest {
    private String name;
    private String gender;
    private Long parentId;
}