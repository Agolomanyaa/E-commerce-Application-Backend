package com.tandogan.ecommerce_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderItemRequest {
    // DÜZELTME: İsimlendirmeyi netleştirmek için eklendi.
    @JsonProperty("variantId")
    private Long variantId;

    @JsonProperty("count")
    private int count;
}