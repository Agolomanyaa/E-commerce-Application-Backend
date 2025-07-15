package com.tandogan.ecommerce_backend.service.impl;

import com.tandogan.ecommerce_backend.dto.request.LoginRequest;
import com.tandogan.ecommerce_backend.dto.request.RegisterRequest;
import com.tandogan.ecommerce_backend.dto.response.LoginResponse;
import com.tandogan.ecommerce_backend.exception.EmailAlreadyExistsException;
import com.tandogan.ecommerce_backend.model.Role;
import com.tandogan.ecommerce_backend.model.User;
import com.tandogan.ecommerce_backend.repository.UserRepository;
import com.tandogan.ecommerce_backend.service.AuthService;
import com.tandogan.ecommerce_backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Bu annotation, final alanlar için constructor'ı OTOMATİK olarak oluşturur.
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Manuel constructor'ı sildik. @RequiredArgsConstructor bu işi zaten yapıyor.

    @Override
    public void register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistsException("Error: Email is already in use!");
        });

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setSurname(request.getSurname());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(Role.USER);

        userRepository.save(newUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. AuthenticationManager'ı kullanarak kullanıcıyı doğrula.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Kullanıcıyı veritabanından bul.
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        // 3. Kullanıcı için bir JWT oluştur.
        var jwtToken = jwtService.generateToken(user);

        // 4. Token'ı bir LoginResponse DTO'su içinde döndür.
        return LoginResponse.builder().token(jwtToken).build();
    }
}