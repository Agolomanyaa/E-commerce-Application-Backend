package com.tandogan.ecommerce_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("product-images")
    @Builder.Default
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("product-variants")
    @Builder.Default
    private Set<ProductVariant> variants = new HashSet<>();

    @Column(nullable = false, columnDefinition = "int default 0")
    @Builder.Default
    private int sellCount = 0;

    @Column(nullable = false, columnDefinition = "float default 0.0")
    @Builder.Default
    private double rating = 0.0; // Düzeltme: float'tan double'a çevrildi.

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    // YENİ EKLENEN METOT
    public int getTotalStock() {
        if (this.variants == null) {
            return 0;
        }
        return this.variants.stream()
                .mapToInt(ProductVariant::getStock)
                .sum();
    }
}