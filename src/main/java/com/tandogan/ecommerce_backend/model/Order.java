package com.tandogan.ecommerce_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders") // "order" SQL'de özel bir anahtar kelime olduğu için tablo adını "orders" yapmak daha güvenlidir.
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Siparişi veren kullanıcı.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Siparişin içindeki ürünler.
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    // Siparişin teslim edileceği adresin bir kopyası.
    // Adres silinse bile sipariş kaydında kalması için doğrudan adres bilgilerini buraya da yazıyoruz.
    @Column(name = "shipping_address", nullable = false, columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    // Sipariş durumlarını yönetmek için bir Enum oluşturalım.
    public enum OrderStatus {
        PENDING,       // Beklemede
        PROCESSING,    // İşleniyor
        SHIPPED,       // Kargolandı
        DELIVERED,     // Teslim Edildi
        CANCELLED      // İptal Edildi
    }
}