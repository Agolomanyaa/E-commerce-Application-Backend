package com.tandogan.ecommerce_backend.controller;

import com.tandogan.ecommerce_backend.dto.request.AddressRequest;
import com.tandogan.ecommerce_backend.dto.response.AddressDto;
import com.tandogan.ecommerce_backend.model.User;
import com.tandogan.ecommerce_backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses") // Tüm adres endpoint'leri bu yolla başlayacak.
@RequiredArgsConstructor

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})

public class AddressController {

    private final AddressService addressService;

    // GİRİŞ YAPMIŞ KULLANICININ TÜM ADRESLERİNİ GETİRME
    @GetMapping
    public ResponseEntity<List<AddressDto>> getMyAddresses(@AuthenticationPrincipal User user) {
        List<AddressDto> addresses = addressService.getAddressesByUserId(user.getId());
        return ResponseEntity.ok(addresses);
    }

    // YENİ BİR ADRES OLUŞTURMA
    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@AuthenticationPrincipal User user, @RequestBody AddressRequest request) {
        AddressDto createdAddress = addressService.createAddress(user.getId(), request);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    // MEVCUT BİR ADRESİ GÜNCELLEME
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(
            @AuthenticationPrincipal User user,
            @PathVariable Long addressId,
            @RequestBody AddressRequest request) {
        AddressDto updatedAddress = addressService.updateAddress(user.getId(), addressId, request);
        return ResponseEntity.ok(updatedAddress);
    }

    // BİR ADRESİ SİLME
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal User user,
            @PathVariable Long addressId) {
        addressService.deleteAddress(user.getId(), addressId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}