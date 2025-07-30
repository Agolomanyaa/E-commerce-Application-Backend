package com.tandogan.ecommerce_backend.controller;

import com.tandogan.ecommerce_backend.dto.request.CreateCategoryRequest;
import com.tandogan.ecommerce_backend.dto.request.UpdateCategoryRequest;
import com.tandogan.ecommerce_backend.dto.response.CategoryDto;
import com.tandogan.ecommerce_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryRequest request) {
        CategoryDto createdCategory = categoryService.createCategory(request);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/form-list")
    public ResponseEntity<List<CategoryDto>> getCategoriesForForm() {
        List<CategoryDto> categories = categoryService.getSubCategoriesForForm();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryRequest request) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(@PathVariable Long id) {
        Map<String, Boolean> response = categoryService.deleteCategory(id);
        return ResponseEntity.ok(response);
    }
}