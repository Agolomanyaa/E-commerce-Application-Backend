package com.tandogan.ecommerce_backend.service.impl;

import com.tandogan.ecommerce_backend.dto.request.CreateProductRequest;
import com.tandogan.ecommerce_backend.dto.request.UpdateProductRequest;
import com.tandogan.ecommerce_backend.dto.request.VariantRequest;
import com.tandogan.ecommerce_backend.dto.response.CategoryDto;
import com.tandogan.ecommerce_backend.dto.response.ProductDto;
import com.tandogan.ecommerce_backend.dto.response.ProductImageDto;
import com.tandogan.ecommerce_backend.dto.response.ProductVariantDto;
import com.tandogan.ecommerce_backend.exception.ResourceNotFoundException;
import com.tandogan.ecommerce_backend.model.Category;
import com.tandogan.ecommerce_backend.model.Product;
import com.tandogan.ecommerce_backend.model.ProductImage;
import com.tandogan.ecommerce_backend.model.ProductVariant;
import com.tandogan.ecommerce_backend.repository.CategoryRepository;
import com.tandogan.ecommerce_backend.repository.ProductRepository;
import com.tandogan.ecommerce_backend.service.ProductService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductDto createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(category)
                .active(true)
                .build();

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            request.getImageUrls().forEach(url -> product.getImages().add(ProductImage.builder().url(url).product(product).build()));
        }

        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            request.getVariants().forEach(variantRequest -> {
                ProductVariant variant = ProductVariant.builder()
                        .color(variantRequest.getColor())
                        .size(variantRequest.getSize())
                        .stock(variantRequest.getStock())
                        .product(product)
                        .build();
                product.getVariants().add(variant);
            });
        }

        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(Long categoryId, String gender, String filterText, String sort, int limit, int offset, Boolean includeInactive) {
        Sort sortObj = Sort.by(Sort.Direction.ASC, "id");
        if (sort != null && !sort.isEmpty()) {
            try {
                String[] sortParams = sort.split(":");
                if (sortParams.length == 2) {
                    sortObj = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
                }
            } catch (Exception e) {
                System.err.println("Invalid sort parameter: " + sort);
            }
        }

        Pageable pageable = PageRequest.of(offset / limit, limit, sortObj);

        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (includeInactive == null || !includeInactive) {
                predicates.add(criteriaBuilder.isTrue(root.get("active")));
            }

            // --- AKILLI KATEGORİ FİLTRELEME GÜNCELLEMESİ ---
            if (categoryId != null) {
                // Ana kategori ve tüm alt kategorilerinin ID'lerini al.
                Set<Long> categoryIdsToFilter = categoryRepository.findSelfAndDescendantIds(categoryId);

                // Eğer ID listesi boş değilse, bu listedeki ID'lere sahip tüm ürünleri getir.
                if (categoryIdsToFilter != null && !categoryIdsToFilter.isEmpty()) {
                    predicates.add(root.get("category").get("id").in(categoryIdsToFilter));
                } else {
                    // Eğer bir kategori ID'si gelip de ona ait alt kategori bulunamazsa
                    // (veya kategori yoksa), hiçbir ürün getirme.
                    predicates.add(criteriaBuilder.equal(root.get("category").get("id"), -1L));
                }
            }
            // --- GÜNCELLEME SONU ---

            if (gender != null && !gender.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("gender"), gender));
            }
            if (filterText != null && !filterText.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filterText.toLowerCase() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> productPage = productRepository.findAll(spec, pageable);
        List<Long> productIds = productPage.getContent().stream().map(Product::getId).toList();

        if (productIds.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, productPage.getTotalElements());
        }

        List<Product> productsWithDetails = productRepository.findWithDetailsByIds(productIds);
        Map<Long, Product> detailsMap = productsWithDetails.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<ProductDto> dtoList = productIds.stream()
                .map(detailsMap::get)
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, productPage.getTotalElements());
    }

    @Override
    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<ProductDto> getProductsByCategoryId(Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId)
                .stream()
                // .filter(Product::isActive) // Bu metod zaten service'de filtreleniyor.
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductDto updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        product.setActive(request.isActive());

        product.getImages().clear();
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            request.getImageUrls().forEach(url -> product.getImages().add(ProductImage.builder().url(url).product(product).build()));
        }

        Map<String, ProductVariant> existingVariantsMap = product.getVariants().stream()
                .collect(Collectors.toMap(v -> v.getColor() + "||" + v.getSize(), Function.identity()));

        Set<ProductVariant> updatedOrNewVariants = new HashSet<>();

        if (request.getVariants() != null) {
            for (VariantRequest variantRequest : request.getVariants()) {
                String key = variantRequest.getColor() + "||" + variantRequest.getSize();
                ProductVariant existingVariant = existingVariantsMap.get(key);

                if (existingVariant != null) {
                    existingVariant.setStock(variantRequest.getStock());
                    updatedOrNewVariants.add(existingVariant);
                    existingVariantsMap.remove(key);
                } else {
                    ProductVariant newVariant = ProductVariant.builder()
                            .color(variantRequest.getColor())
                            .size(variantRequest.getSize())
                            .stock(variantRequest.getStock())
                            .product(product)
                            .build();
                    updatedOrNewVariants.add(newVariant);
                }
            }
        }
        product.getVariants().clear();
        product.getVariants().addAll(updatedOrNewVariants);

        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }


    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + productId));

        productRepository.delete(product);
    }

    private ProductDto convertToDto(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .rating(product.getRating())
                .sellCount(product.getSellCount())
                .totalStock(product.getTotalStock())
                .active(product.isActive())
                .category(CategoryDto.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .gender(product.getCategory().getGender())
                        .build())
                .images(product.getImages().stream()
                        .map(image -> new ProductImageDto(image.getId(), image.getUrl()))
                        .collect(Collectors.toList()))
                .variants(product.getVariants().stream()
                        .map(variant -> ProductVariantDto.builder()
                                .id(variant.getId())
                                .color(variant.getColor())
                                .size(variant.getSize())
                                .stock(variant.getStock())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}