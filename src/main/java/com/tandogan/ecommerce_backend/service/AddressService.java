package com.tandogan.ecommerce_backend.service;

import com.tandogan.ecommerce_backend.dto.request.AddressRequest;
import com.tandogan.ecommerce_backend.dto.response.AddressDto;

import java.util.List;

public interface AddressService {

    /**
     * Belirtilen kullanıcı için yeni bir adres oluşturur.
     * @param userId Adresin ekleneceği kullanıcının ID'si.
     * @param request Eklenecek adresin bilgilerini içeren istek.
     * @return Oluşturulan adresin DTO'su.
     */
    AddressDto createAddress(Long userId, AddressRequest request);

    /**
     * Belirtilen kullanıcıya ait tüm adresleri listeler.
     * @param userId Adresleri listelenecek kullanıcının ID'si.
     * @return Kullanıcıya ait adreslerin bir listesi.
     */
    List<AddressDto> getAddressesByUserId(Long userId);

    /**
     * Mevcut bir adresi günceller.
     * @param userId İşlemi yapan kullanıcının ID'si (güvenlik için).
     * @param addressId Güncellenecek adresin ID'si.
     * @param request Adresin yeni bilgilerini içeren istek.
     * @return Güncellenmiş adresin DTO'su.
     */
    AddressDto updateAddress(Long userId, Long addressId, AddressRequest request);

    /**
     * Bir adresi siler.
     * @param userId İşlemi yapan kullanıcının ID'si (güvenlik için).
     * @param addressId Silinecek adresin ID'si.
     */
    void deleteAddress(Long userId, Long addressId);
}