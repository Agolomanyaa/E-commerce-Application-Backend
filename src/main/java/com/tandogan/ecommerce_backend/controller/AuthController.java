package com.tandogan.ecommerce_backend.controller;

import com.tandogan.ecommerce_backend.dto.request.LoginRequest;
import com.tandogan.ecommerce_backend.dto.request.RegisterRequest;
import com.tandogan.ecommerce_backend.dto.response.LoginResponse;
import com.tandogan.ecommerce_backend.model.User;
import com.tandogan.ecommerce_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    // YENİ EKLENEN ENDPOINT: Token doğrulama için kullanılır.
    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyToken() {
        // Bu metoda ulaşıldığında, JwtAuthFilter zaten token'ı doğrulamış demektir.
        // Güvenlik bağlamından (security context) kimliği doğrulanmış kullanıcıyı alabiliriz.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Frontend'in beklediği kullanıcı verisi formatını (token hariç) oluşturuyoruz.
        Map<String, Object> userData = Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "surname", user.getSurname(),
                "email", user.getEmail(),
                "role", user.getRole()
        );

        return ResponseEntity.ok(userData);
    }
}