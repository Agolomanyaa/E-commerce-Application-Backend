package com.tandogan.ecommerce_backend.controller;

import com.tandogan.ecommerce_backend.dto.request.CreateProductRequest;
import com.tandogan.ecommerce_backend.dto.request.UpdateProductRequest;
import com.tandogan.ecommerce_backend.dto.response.ProductDto;
import com.tandogan.ecommerce_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
// DİKKAT: Bütün sorun bu satırın güncellenmesiyle çözülecek.
// Frontend'in beklediği ve SecurityConfig'in koruduğu "/api/v1" ön ekini ekliyoruz.
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest request) {
        ProductDto newProduct = productService.createProduct(request);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String filterText,
            @RequestParam(defaultValue = "id:asc") String sort,
            @RequestParam(defaultValue = "24") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) Boolean includeInactive
    ) {
        Page<ProductDto> productPage = productService.getAllProducts(categoryId, gender, filterText, sort, limit, offset, includeInactive);
        Map<String, Object> response = Map.of(
                "products", productPage.getContent(),
                "total", productPage.getTotalElements()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest request) {
        ProductDto updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}