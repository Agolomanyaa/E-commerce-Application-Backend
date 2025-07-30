package com.tandogan.ecommerce_backend.dto.request;

import lombok.Data;

@Data
public class VariantRequest {
    private String color;
    private String size;
    private int stock;
}