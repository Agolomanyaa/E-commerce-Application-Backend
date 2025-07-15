package com.tandogan.ecommerce_backend.repository;

import com.tandogan.ecommerce_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findAllByCategoryId(Long categoryId);

    // YENİ METOT: "Stok Tükendi" sorununu kalıcı olarak çözmek için.
    // Bu sorgu, ürünleri çekerken onlara bağlı olan 'variants' ve 'images'
    // listelerini de TEK BİR VERİTABANI SORGUSUNDA getirmeye zorlar (JOIN FETCH).
    // 'DISTINCT' anahtar kelimesi, join işlemlerinden kaynaklanabilecek
    // mükerrer ürün kayıtlarını engeller.
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.variants LEFT JOIN FETCH p.images WHERE p.id IN :ids")
    List<Product> findWithDetailsByIds(@Param("ids") List<Long> ids);
}