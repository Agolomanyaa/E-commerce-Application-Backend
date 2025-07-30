package com.tandogan.ecommerce_backend.repository;

import com.tandogan.ecommerce_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByParentIsNotNull(); // YENİ METOD
}