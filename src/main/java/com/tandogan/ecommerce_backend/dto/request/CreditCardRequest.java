package com.tandogan.ecommerce_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreditCardRequest {
    @JsonProperty("card_no")
    private String cardNo;

    @JsonProperty("expire_month")
    private Integer expireMonth;

    @JsonProperty("expire_year")
    private Integer expireYear;

    @JsonProperty("name_on_card")
    private String nameOnCard;

    // CCV alanı DTO'da var ama entity'ye kaydedilmeyecek.
    private String ccv;
}