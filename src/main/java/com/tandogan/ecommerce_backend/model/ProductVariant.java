package com.tandogan.ecommerce_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_variants")
// DEĞİŞİKLİK: @Data yerine daha kontrollü anotasyonlar eklendi
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "product")      // Bu satır sonsuz toString döngüsünü engeller
@EqualsAndHashCode(exclude = "product") // Bu satır sonsuz hashCode/equals döngüsünü (ve StackOverflowError'ı) engeller
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private int stock; // Not: Null olamayacağı için int kullanmak daha güvenli.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference("product-variants")
    private Product product;
}