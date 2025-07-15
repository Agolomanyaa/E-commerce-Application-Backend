package com.tandogan.ecommerce_backend.service;

import com.tandogan.ecommerce_backend.dto.request.CreateProductRequest;
import com.tandogan.ecommerce_backend.dto.request.UpdateProductRequest;
import com.tandogan.ecommerce_backend.dto.response.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDto createProduct(CreateProductRequest request);

    Page<ProductDto> getAllProducts(Long categoryId, String gender, String filterText, String sort, int limit, int offset, Boolean includeInactive);

    Optional<ProductDto> getProductById(Long id);

    List<ProductDto> getProductsByCategoryId(Long categoryId);

    ProductDto updateProduct(Long id, UpdateProductRequest request);

    // DİKKAT: Metodun imzası, ProductServiceImpl ile uyumlu olacak şekilde
    // void olarak güncellendi. Hatanın çözümü bu satırdır.
    void deleteProduct(Long id);
}