package com.tandogan.ecommerce_backend.config;

import com.tandogan.ecommerce_backend.model.*;
import com.tandogan.ecommerce_backend.repository.CategoryRepository;
import com.tandogan.ecommerce_backend.repository.ProductRepository;
import com.tandogan.ecommerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            createAdminUser();
        }
        if (productRepository.count() == 0) {
            loadInitialProducts();
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

    private void loadInitialProducts() {
        System.out.println(">>> Loading initial products into the database...");

        Category kadin = categoryRepository.save(Category.builder().name("Kadın").gender("k").build());
        Category erkek = categoryRepository.save(Category.builder().name("Erkek").gender("e").build());
        Category homeLiving = categoryRepository.save(Category.builder().name("Home & Living").gender("u").build());
        Product kadinTshirt = Product.builder()
                .name("Kadın Basic Tişört")
                .description("Yumuşak pamuklu kumaştan, her mevsime uygun.")
                .price(new BigDecimal("350.00"))
                .category(kadin)
                .build();
        kadinTshirt.getImages().add(ProductImage.builder().url("https://picsum.photos/id/1027/800/800").product(kadinTshirt).build());
        addVariants(kadinTshirt, List.of("Beyaz", "Siyah"), List.of("S", "M", "L"), 50);
        productRepository.save(kadinTshirt);

        Product erkekGomlek = Product.builder()
                .name("Erkek Oduncu Gömlek")
                .description("Kırmızı-siyah ekose desenli, kalın kumaş.")
                .price(new BigDecimal("750.00"))
                .category(erkek)
                .build();
        erkekGomlek.getImages().add(ProductImage.builder().url("https://picsum.photos/id/1084/800/800").product(erkekGomlek).build());
        addVariants(erkekGomlek, List.of("Kırmızı", "Mavi"), List.of("M", "L", "XL"), 30);
        productRepository.save(erkekGomlek);

        System.out.println(">>> Initial products loaded successfully.");
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