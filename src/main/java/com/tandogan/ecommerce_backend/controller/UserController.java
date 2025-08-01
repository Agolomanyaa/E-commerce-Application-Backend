package com.tandogan.ecommerce_backend.controller;

import com.tandogan.ecommerce_backend.dto.request.AddressRequest;
import com.tandogan.ecommerce_backend.dto.request.CreditCardRequest;
import com.tandogan.ecommerce_backend.dto.response.AddressDto;
import com.tandogan.ecommerce_backend.dto.response.CreditCardDto;
import com.tandogan.ecommerce_backend.exception.ResourceNotFoundException;
import com.tandogan.ecommerce_backend.model.Address;
import com.tandogan.ecommerce_backend.model.CreditCard;
import com.tandogan.ecommerce_backend.model.User;
import com.tandogan.ecommerce_backend.repository.AddressRepository;
import com.tandogan.ecommerce_backend.repository.CreditCardRepository;
import com.tandogan.ecommerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CreditCardRepository creditCardRepository; // YENİ EKLENEN

    // --- Address Endpoints ---
    @GetMapping("/address")
    public ResponseEntity<List<AddressDto>> getUserAddresses() {
        User user = getCurrentAuthenticatedUser();
        List<AddressDto> addresses = addressRepository.findByUserId(user.getId())
                .stream()
                .map(this::convertAddressToDto) // Metod adı netlik için güncellendi
                .collect(Collectors.toList());
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/address")
    public ResponseEntity<AddressDto> addAddress(@RequestBody AddressRequest addressRequest) {
        User user = getCurrentAuthenticatedUser();

        Address address = Address.builder()
                .title(addressRequest.getTitle())
                .name(addressRequest.getName())
                .surname(addressRequest.getSurname())
                .phone(addressRequest.getPhone())
                .city(addressRequest.getCity())
                .district(addressRequest.getDistrict())
                .neighborhood(addressRequest.getNeighborhood())
                .user(user)
                .build();

        Address savedAddress = addressRepository.save(address);

        return new ResponseEntity<>(convertAddressToDto(savedAddress), HttpStatus.CREATED); // Metod adı netlik için güncellendi
    }

    // --- YENİ: Credit Card Endpoints ---
    @GetMapping("/card")
    public ResponseEntity<List<CreditCardDto>> getUserCreditCards() {
        User user = getCurrentAuthenticatedUser();
        List<CreditCardDto> cards = creditCardRepository.findByUserId(user.getId())
                .stream()
                .map(this::convertCardToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cards);
    }

    @PostMapping("/card")
    public ResponseEntity<CreditCardDto> addCreditCard(@RequestBody CreditCardRequest cardRequest) {
        User user = getCurrentAuthenticatedUser();

        CreditCard card = CreditCard.builder()
                .cardNo(cardRequest.getCardNo())
                .expireMonth(cardRequest.getExpireMonth())
                .expireYear(cardRequest.getExpireYear())
                .nameOnCard(cardRequest.getNameOnCard())
                .user(user)
                .build();

        CreditCard savedCard = creditCardRepository.save(card);

        return new ResponseEntity<>(convertCardToDto(savedCard), HttpStatus.CREATED);
    }


    // --- Helper Metodlar ---
    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));
    }

    private AddressDto convertAddressToDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .title(address.getTitle())
                .name(address.getName())
                .surname(address.getSurname())
                .phone(address.getPhone())
                .city(address.getCity())
                .district(address.getDistrict())
                .neighborhood(address.getNeighborhood())
                .build();
    }

    private CreditCardDto convertCardToDto(CreditCard card) {
        return CreditCardDto.builder()
                .id(card.getId())
                .cardNo(card.getCardNo())
                .expireMonth(card.getExpireMonth())
                .expireYear(card.getExpireYear())
                .nameOnCard(card.getNameOnCard())
                .build();
    }
}