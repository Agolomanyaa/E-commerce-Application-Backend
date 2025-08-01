package com.tandogan.ecommerce_backend.service.impl;

import com.tandogan.ecommerce_backend.dto.request.AddressRequest;
import com.tandogan.ecommerce_backend.dto.response.AddressDto;
import com.tandogan.ecommerce_backend.exception.ResourceNotFoundException;
import com.tandogan.ecommerce_backend.model.Address;
import com.tandogan.ecommerce_backend.model.User;
import com.tandogan.ecommerce_backend.repository.AddressRepository;
import com.tandogan.ecommerce_backend.repository.UserRepository;
import com.tandogan.ecommerce_backend.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AddressDto createAddress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // DTO'dan gelen bilgilerle yeni bir Address nesnesi oluştur.
        Address address = Address.builder()
                .user(user)
                .title(request.getTitle()) // DÜZELTME: getAddressTitle -> getTitle
                .name(request.getName())
                .surname(request.getSurname())
                .phone(request.getPhone())
                .city(request.getCity())
                .district(request.getDistrict())
                .neighborhood(request.getNeighborhood())
                // KALDIRILDI: .fullAddress(request.getFullAddress())
                .build();

        Address savedAddress = addressRepository.save(address);

        return convertToDto(savedAddress);
    }

    @Override
    public List<AddressDto> getAddressesByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto updateAddress(Long userId, Long addressId, AddressRequest request) {
        Address addressToUpdate = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!addressToUpdate.getUser().getId().equals(userId)) {
            throw new SecurityException("User does not have permission to update this address.");
        }

        addressToUpdate.setTitle(request.getTitle()); // DÜZELTME: setAddressTitle -> setTitle
        addressToUpdate.setName(request.getName());
        addressToUpdate.setSurname(request.getSurname());
        addressToUpdate.setPhone(request.getPhone());
        addressToUpdate.setCity(request.getCity());
        addressToUpdate.setDistrict(request.getDistrict());
        addressToUpdate.setNeighborhood(request.getNeighborhood());
        // KALDIRILDI: addressToUpdate.setFullAddress(request.getFullAddress());

        Address updatedAddress = addressRepository.save(addressToUpdate);
        return convertToDto(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address addressToDelete = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!addressToDelete.getUser().getId().equals(userId)) {
            throw new SecurityException("User does not have permission to delete this address.");
        }

        addressRepository.delete(addressToDelete);
    }

    private AddressDto convertToDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .title(address.getTitle()) // DÜZELTME: getAddressTitle -> getTitle
                .name(address.getName())
                .surname(address.getSurname())
                .phone(address.getPhone())
                .city(address.getCity())
                .district(address.getDistrict())
                .neighborhood(address.getNeighborhood())
                // KALDIRILDI: .fullAddress(address.getFullAddress())
                .build();
    }
}