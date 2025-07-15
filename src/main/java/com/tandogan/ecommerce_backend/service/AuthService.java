package com.tandogan.ecommerce_backend.service;

import com.tandogan.ecommerce_backend.dto.request.LoginRequest;
import com.tandogan.ecommerce_backend.dto.request.RegisterRequest;
import com.tandogan.ecommerce_backend.dto.response.LoginResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest); // <-- YENİ METOD
}