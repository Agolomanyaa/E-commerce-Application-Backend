package com.tandogan.ecommerce_backend.repository;

import com.tandogan.ecommerce_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Belirtilen kullanıcı ID'sine ait tüm siparişleri, en yeniden en eskiye doğru sıralanmış şekilde bulur.
     * @param userId Siparişleri aranacak kullanıcının ID'si.
     * @return Kullanıcıya ait siparişlerin bir listesi.
     */
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);
}