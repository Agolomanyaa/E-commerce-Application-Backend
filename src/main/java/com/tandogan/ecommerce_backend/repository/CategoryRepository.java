package com.tandogan.ecommerce_backend.repository;

import com.tandogan.ecommerce_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByParentIsNotNull();

    /**
     * Verilen bir kategori ID'sinin kendisi ve tüm alt kategorilerinin (çocuk, torun vb.)
     * ID'lerini bulan recursive bir sorgu. Bu, hiyerarşik filtreleme için kullanılır.
     * @param categoryId Başlangıç ana kategori ID'si.
     * @return Kendisi ve tüm alt kategorilerinin ID'lerini içeren bir Set.
     */
    @Query(value = "WITH RECURSIVE category_tree AS (" +
            "  SELECT id FROM categories WHERE id = :categoryId" +
            "  UNION ALL" +
            // --- HATA BURADAYDI, DÜZELTİLDİ ---
            // `parent_id` yerine veritabanındaki doğru sütun adı olan `parent_category_id` kullanılıyor.
            "  SELECT c.id FROM categories c JOIN category_tree ct ON c.parent_category_id = ct.id" +
            ") SELECT id FROM category_tree", nativeQuery = true)
    Set<Long> findSelfAndDescendantIds(@Param("categoryId") Long categoryId);
}