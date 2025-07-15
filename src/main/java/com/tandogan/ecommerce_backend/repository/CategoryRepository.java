package com.tandogan.ecommerce_backend.repository;

import com.tandogan.ecommerce_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// findByCode ve diğer eski metotlar kaldırıldı.
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}