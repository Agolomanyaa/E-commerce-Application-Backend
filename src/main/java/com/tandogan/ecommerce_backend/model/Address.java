package com.tandogan.ecommerce_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bu adresin hangi kullanıcıya ait olduğu.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Adresi gösterirken tekrar kullanıcıyı göstermeyelim, döngü oluşmasın.
    private User user;

    @Column(name = "address_title", nullable = false)
    private String addressTitle; // "Ev Adresim", "İş Adresim" gibi

    @Column(nullable = false)
    private String name; // Alıcı adı (Tandoğan)

    @Column(nullable = false)
    private String surname; // Alıcı soyadı (Goncu)

    @Column(nullable = false)
    private String phone; // Telefon

    @Column(nullable = false)
    private String city; // Şehir

    @Column(nullable = false)
    private String district; // İlçe

    @Column(nullable = false)
    private String neighborhood; // Mahalle

    @Column(name = "full_address", nullable = false, columnDefinition = "TEXT")
    private String fullAddress; // Açık adres
}