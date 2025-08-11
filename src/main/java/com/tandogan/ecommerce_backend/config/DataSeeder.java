package com.tandogan.ecommerce_backend.config;

import com.tandogan.ecommerce_backend.model.*;
import com.tandogan.ecommerce_backend.repository.CategoryRepository;
import com.tandogan.ecommerce_backend.repository.ProductRepository;
import com.tandogan.ecommerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        boolean noUsers = userRepository.count() == 0;
        boolean noCategories = categoryRepository.count() == 0;
        boolean noProducts = productRepository.count() == 0;

        if (noUsers) {
            createAdminUser();
        }

        if (noCategories && noProducts) {
            loadInitialData();
        }
    }

    private void createAdminUser() {
        User admin = User.builder()
                .name("Admin")
                .surname("User")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
        System.out.println(">>> Admin user created successfully!");
    }

    private void loadInitialData() {
        System.out.println(">>> Loading initial categories and products into the database...");

        // Ana Kategoriler
        Category kadinParent = createCategory("Kadın", "k", null);
        Category erkekParent = createCategory("Erkek", "e", null);
        Category homeParent = createCategory("Home & Living", "u", null);

        // Alt Kategoriler
        Category erkekGomlekCategory = createCategory("Erkek Gömlek", "e", erkekParent);
        Category erkekPantalonCategory = createCategory("Erkek Pantalon", "e", erkekParent);
        createCategory("Erkek Ceket", "e", erkekParent);
        createCategory("Erkek T-shirt", "e", erkekParent);
        createCategory("Erkek Şort", "e", erkekParent);

        Category kadinGomlekCategory = createCategory("Kadın Gömlek", "k", kadinParent);
        Category kadinPantalonCategory = createCategory("Kadın Pantalon", "k", kadinParent);
        createCategory("Kadın Elbise", "k", kadinParent);
        createCategory("Kadın Etek", "k", kadinParent);
        createCategory("Kadın Ceket", "k", kadinParent);

        Category nevresimCategory = createCategory("Nevresim Takımı", "u", homeParent);
        Category yatakOrtusuCategory = createCategory("Yatak Örtüsü", "u", homeParent);

        // --- GÜNCELLENMİŞ ÜRÜNLER ---

        createProduct("Kadın Basic Tişört", "Yumuşak pamuklu kumaştan, her mevsime uygun.", new BigDecimal("350.00"), kadinGomlekCategory,
                List.of(
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/a8ba1fb5-5ca8-4378-8c9f-d937dd6c5756/3840/c25008-2.webp",
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/15aed17d-c71c-4eef-9be4-5997fabb6339/3840/mar-c25008-1.webp",
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/d26c62ca-e83c-44cb-ad96-7d7da1008bb1/3840/c25008-1.webp"

                ), List.of("Beyaz", "Siyah"), List.of("S", "M", "L"), 100);

        createProduct("Erkek Jean Gömlek", "Kırmızı-siyah ekose desenli, kalın kumaş.", new BigDecimal("750.00"), erkekGomlekCategory,
                List.of(
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/27100752-a302-42dd-aedb-c0bf21403fcb/3840/b21-2509-999.webp",
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/99e80c25-fb5e-4d95-b22e-a8d2d657d886/3840/b21-2509-999-17.webp",
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/46ae0546-f208-4e6f-afa4-cd6cf478ce1a/3840/b21-2509-999-14.webp"
                ), List.of("Mavi"), List.of("M", "L", "XL"), 60);

        createProduct("Kadın Yüksek Bel Jean Şort", "Kaliteli denim kumaştan üretilmiştir.", new BigDecimal("550.00"), kadinPantalonCategory,
                List.of(
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/496cfa2e-df94-47e5-8dd7-28c4eaeb4277/3840/bw-h10394-1.webp",
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/797eee65-eb06-49cd-b211-c49efb5126d3/3840/bw-h10394-mavi-1.webp",
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/0640a87d-2a0a-4a4b-9c91-b7082ae1ca08/3840/bw-h10394-mavi-2.webp"
                ), List.of("Açık Mavi"), List.of("S", "M", "L", "XL"), 80);

        createProduct("Erkek Skinny Fit Pantolon", "Likralı mavi jean.", new BigDecimal("850.00"), erkekPantalonCategory,
                List.of(
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/66014115-d9d7-4b27-a7e4-f5b4ac611817/3840/bnny-25006.webp",
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/14e1b57b-0b59-4e45-8ebc-108d70b4c44f/3840/bnny-25006-3.webp",
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/769d6884-76d0-4650-95cc-21d6720bc0c5/3840/bnny-25006-7.webp"
                ), List.of("Mavi"), List.of("30", "32", "34"), 120);

        createProduct("Çift Kişilik Yeşil Nevresim", "Ranforce Kumaş, %100 Pamuk, Çizgili Desen", new BigDecimal("1250.99"), nevresimCategory,
                List.of(
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/56e38b38-e99b-4dcc-bd0c-870ac70157b3/image_3840.webp",
                        "https://cdn.myikas.com/images/41c3d708-7e1f-44e8-8f55-8a3be5a9be11/9bf11990-b5a6-4210-a45f-8acaf7ea6bed/image_3840.webp"
                ), List.of("Yeşil"), List.of("Çift Kişilik"), 50);

        createProduct("Trioline Kahve Çift Kişilik Nevresim", "Ranforce Kumaş, %100 Pamuk, Kahve Tonları", new BigDecimal("1300.50"), nevresimCategory,
                List.of("https://i.imgur.com/exampleBrownSheet.jpeg"), List.of("Kahve"), List.of("Çift Kişilik"), 45);

        createProduct("Pike Yatak Örtüsü Antrasit", "Yumuşak dokulu pamuk pike", new BigDecimal("980.00"), yatakOrtusuCategory,
                List.of("https://i.imgur.com/exampleBedspread.jpeg"), List.of("Antrasit"), List.of("Tek Kişilik", "Çift Kişilik"), 70);

        System.out.println(">>> Initial data loaded successfully.");
    }

    private Category createCategory(String name, String gender, Category parent) {
        Category category = Category.builder()
                .name(name)
                .gender(gender)
                .parent(parent)
                .build();
        return categoryRepository.save(category);
    }

    private void createProduct(String name, String description, BigDecimal price, Category category, List<String> imageUrls, List<String> colors, List<String> sizes, int initialStock) {
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .active(true)
                .build();

        // Birden fazla resim URL'sini işle
        if (imageUrls != null && !imageUrls.isEmpty()) {
            imageUrls.forEach(url -> product.getImages().add(ProductImage.builder().url(url).product(product).build()));
        }

        addVariants(product, colors, sizes, initialStock);
        productRepository.save(product);
    }

    private void addVariants(Product product, List<String> colors, List<String> sizes, int initialStock) {
        for (String color : colors) {
            for (String size : sizes) {
                ProductVariant variant = ProductVariant.builder()
                        .product(product)
                        .color(color)
                        .size(size)
                        .stock(initialStock)
                        .build();
                product.getVariants().add(variant);
            }
        }
    }
}
// Son kontrol