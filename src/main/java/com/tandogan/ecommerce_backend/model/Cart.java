package com.tandogan.ecommerce_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sepetin hangi kullanıcıya ait olduğu.
    // Bir kullanıcının sadece bir sepeti olacağı için "OneToOne" ilişki kuruyoruz.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Bir sepetin içinde birden çok sepet öğesi olabilir.
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL, // Sepet silinirse, içindeki öğeler de silinsin.
            orphanRemoval = true // Sepetten bir öğe çıkarılırsa, veritabanından da silinsin.
    )
    @Builder.Default // Lombok Builder'ın bu alanı boş geçmemesi için.
    private List<CartItem> cartItems = new ArrayList<>();

    // Bu alan veritabanında bir sütun olarak tutulmayacak.
    // Her istendiğinde, içindeki cartItems'a bakılarak dinamik olarak hesaplanacak.
    @Transient
    private BigDecimal totalPrice;
}