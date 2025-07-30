package com.tandogan.ecommerce_backend.service;

import com.tandogan.ecommerce_backend.dto.request.CreateCategoryRequest;
import com.tandogan.ecommerce_backend.dto.request.UpdateCategoryRequest;
import com.tandogan.ecommerce_backend.dto.response.CategoryDto;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    CategoryDto createCategory(CreateCategoryRequest request);
    List<CategoryDto> getAllCategories();
    List<CategoryDto> getSubCategoriesForForm(); // YENİ METOD
    CategoryDto getCategoryById(Long id);
    CategoryDto updateCategory(Long id, UpdateCategoryRequest request);
    Map<String, Boolean> deleteCategory(Long id);
}