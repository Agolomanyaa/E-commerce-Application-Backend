package com.tandogan.ecommerce_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"parent", "children", "products"})
@EqualsAndHashCode(exclude = {"parent", "children", "products"})
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1)
    private String gender;

    // --- YENİ ALANLAR ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    @JsonIgnore
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default // --- DÜZELTME 1 ---
    private Set<Category> children = new HashSet<>();
    // --- BİTİŞ ---

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // --- DÜZELTME 2 ---
    @Builder.Default // --- DÜZELTME 1 ---
    private Set<Product> products = new HashSet<>();
}