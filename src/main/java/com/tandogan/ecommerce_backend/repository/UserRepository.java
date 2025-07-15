package com.tandogan.ecommerce_backend.repository;

import com.tandogan.ecommerce_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Bu interface'in bir Spring Bean'i olduğunu belirtir (isteğe bağlı ama iyi bir pratik)
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA, bu metodun isminden ne yapması gerektiğini anlar.
    // "find by Email" -> 'email' alanına göre bir User arar.
    // Optional<User> -> Kullanıcı bulunursa User nesnesini, bulunamazsa boş bir Optional döner.
    // Bu, NullPointerException hatalarını önlemek için modern bir yaklaşımdır.
    Optional<User> findByEmail(String email);
}