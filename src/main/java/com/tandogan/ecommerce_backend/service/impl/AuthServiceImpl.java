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
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistsException("Error: Email is already in use!");
        });

        User newUser = new User();

        // --- İSİM VE SOYİSİM AYIRMA MANTIĞI ---
        String fullName = request.getName().trim();
        int lastSpaceIndex = fullName.lastIndexOf(' ');

        if (lastSpaceIndex > 0) {
            newUser.setName(fullName.substring(0, lastSpaceIndex));
            newUser.setSurname(fullName.substring(lastSpaceIndex + 1));
        } else {
            // Eğer boşluk yoksa, tamamını isim olarak al, soyismi boş bırakma (örneğin "User" ata)
            newUser.setName(fullName);
            newUser.setSurname("User"); // veya farklı bir varsayılan değer
        }
        // --- BİTİŞ ---

        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // Frontend'den gelen rolü al ve enum'a çevir
        try {
            newUser.setRole(Role.valueOf(request.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            // Eğer geçersiz bir rol gelirse, varsayılan olarak USER ata
            newUser.setRole(Role.USER);
        }

        userRepository.save(newUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));
        var jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder().token(jwtToken).build();
    }
}