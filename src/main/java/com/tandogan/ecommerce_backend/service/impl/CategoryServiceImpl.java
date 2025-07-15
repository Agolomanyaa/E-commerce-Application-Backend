package com.tandogan.ecommerce_backend.service.impl;

import com.tandogan.ecommerce_backend.dto.request.CreateCategoryRequest;
import com.tandogan.ecommerce_backend.dto.request.UpdateCategoryRequest;
import com.tandogan.ecommerce_backend.dto.response.CategoryDto;
import com.tandogan.ecommerce_backend.exception.ResourceNotFoundException;
import com.tandogan.ecommerce_backend.model.Category;
import com.tandogan.ecommerce_backend.repository.CategoryRepository;
import com.tandogan.ecommerce_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CreateCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .gender(request.getGender()) // gender bilgisini request'ten al
                .build();
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = findCategoryById(id);
        return convertToDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long id, UpdateCategoryRequest request) {
        Category categoryToUpdate = findCategoryById(id);
        categoryToUpdate.setName(request.getName());
        categoryToUpdate.setGender(request.getGender()); // gender bilgisini güncelle
        Category updatedCategory = categoryRepository.save(categoryToUpdate);
        return convertToDto(updatedCategory);
    }

    @Override
    public Map<String, Boolean> deleteCategory(Long id) {
        Category categoryToDelete = findCategoryById(id);
        categoryRepository.delete(categoryToDelete);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private CategoryDto convertToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .gender(category.getGender()) // DTO'ya gender bilgisini ekle
                .build();
    }
}