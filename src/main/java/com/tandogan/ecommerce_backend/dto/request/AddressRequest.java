package com.tandogan.ecommerce_backend.dto.request;

import lombok.Data;

@Data
public class AddressRequest {
    private String title;
    private String name;
    private String surname;
    private String phone;
    private String city;
    private String district;
    private String neighborhood;
}