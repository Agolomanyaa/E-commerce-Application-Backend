package com.tandogan.ecommerce_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Siparişle ilgisi olmayan alanları (kart bilgileri gibi) görmezden gel
public class CreateOrderRequest {
    @JsonProperty("address_id") // DÜZELTME: Frontend'den gelen 'address_id' alanını 'addressId' ile eşleştirir.
    private Long addressId;

    private BigDecimal price;
    private List<OrderItemRequest> products;
}