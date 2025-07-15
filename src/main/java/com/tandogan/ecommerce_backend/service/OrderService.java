package com.tandogan.ecommerce_backend.service;

import com.tandogan.ecommerce_backend.dto.request.CreateOrderRequest;
import com.tandogan.ecommerce_backend.dto.response.OrderDto;

import java.util.List;

public interface OrderService {

    /**
     * Kullanıcının sepetindeki ürünleri ve seçtiği adresi kullanarak yeni bir sipariş oluşturur.
     * Sipariş oluşturulduktan sonra kullanıcının sepeti temizlenir.
     *
     * @param userId  Siparişi oluşturan kullanıcının ID'si.
     * @param request Sipariş için seçilen adres ID'sini içeren istek.
     * @return Oluşturulan siparişin detaylarını içeren DTO.
     */
    OrderDto createOrder(Long userId, CreateOrderRequest request);

    /**
     * Belirli bir kullanıcının tüm geçmiş siparişlerini getirir.
     *
     * @param userId Siparişleri getirilecek kullanıcının ID'si.
     * @return Kullanıcının sipariş listesini içeren DTO listesi.
     */
    List<OrderDto> getOrdersByUserId(Long userId);
}