package com.tandogan.ecommerce_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder // AuthServiceImpl'de .builder() kullandığımız için bu gerekli
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
}