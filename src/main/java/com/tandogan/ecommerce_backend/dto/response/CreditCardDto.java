package com.tandogan.ecommerce_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditCardDto {
    private Long id;

    @JsonProperty("card_no")
    private String cardNo;

    @JsonProperty("expire_month")
    private Integer expireMonth;

    @JsonProperty("expire_year")
    private Integer expireYear;

    @JsonProperty("name_on_card")
    private String nameOnCard;
}