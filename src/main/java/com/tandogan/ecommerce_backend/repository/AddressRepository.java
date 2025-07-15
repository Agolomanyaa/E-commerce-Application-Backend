package com.tandogan.ecommerce_backend.repository;

import com.tandogan.ecommerce_backend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Belirtilen kullanıcı ID'sine ait tüm adresleri bulur.
     * @param userId Adresleri aranacak kullanıcının ID'si.
     * @return Kullanıcıya ait adreslerin bir listesi.
     */
    List<Address> findByUserId(Long userId);
}