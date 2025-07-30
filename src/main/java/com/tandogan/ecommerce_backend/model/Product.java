package com.tandogan.ecommerce_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString(exclude = {"category", "variants", "images"})
@EqualsAndHashCode(exclude = {"category", "variants", "images"})
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ProductVariant> variants = new HashSet<>();

    @Column(name = "sell_count", nullable = false)
    @Builder.Default
    private int sellCount = 0;

    @Column(name = "rating", nullable = false)
    @Builder.Default
    private double rating = 0.0;

    // --- DEĞİŞİKLİK ---
    // Alanın adını ve kolon adını "active" olarak güncelledik.
    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    public int getTotalStock() {
        if (this.variants == null) {
            return 0;
        }
        return this.variants.stream()
                .mapToInt(ProductVariant::getStock)
                .sum();
    }
}