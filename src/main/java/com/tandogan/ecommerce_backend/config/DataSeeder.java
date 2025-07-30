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
        if (categoryRepository.count() == 0) {
            loadInitialCategoriesAndProducts();
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

    private void loadInitialCategoriesAndProducts() {
        System.out.println(">>> Loading initial categories and products into the database...");

        Category kadinParent = createCategory("Kadın", "k", null);
        Category erkekParent = createCategory("Erkek", "e", null);
        createCategory("Home & Living", "u", null);

        createCategory("Erkek Pantalon", "e", erkekParent);
        Category erkekGomlekCategory = createCategory("Erkek Gömlek", "e", erkekParent);
        createCategory("Erkek Ceket", "e", erkekParent);
        createCategory("Erkek T-shirt", "e", erkekParent);
        createCategory("Erkek Şort", "e", erkekParent);

        createCategory("Kadın Elbise", "k", kadinParent);
        Category kadinGomlekCategory = createCategory("Kadın Gömlek", "k", kadinParent);
        Category kadinPantalonCategory = createCategory("Kadın Pantalon", "k", kadinParent);
        createCategory("Kadın Etek", "k", kadinParent);
        createCategory("Kadın Ceket", "k", kadinParent);

        Product kadinTshirt = Product.builder()
                .name("Kadın Basic Tişört")
                .description("Yumuşak pamuklu kumaştan, her mevsime uygun.")
                .price(new BigDecimal("350.00"))
                .category(kadinGomlekCategory)
                .active(true) // <<< DÜZELTME
                .build();
        kadinTshirt.getImages().add(ProductImage.builder().url("https://picsum.photos/id/1027/800/800").product(kadinTshirt).build());
        addVariants(kadinTshirt, List.of("Beyaz", "Siyah"), List.of("S", "M", "L"), 100);
        productRepository.save(kadinTshirt);

        Product erkekGomlek = Product.builder()
                .name("Erkek Oduncu Gömlek")
                .description("Kırmızı-siyah ekose desenli, kalın kumaş.")
                .price(new BigDecimal("750.00"))
                .category(erkekGomlekCategory)
                .active(true) // <<< DÜZELTME
                .build();
        erkekGomlek.getImages().add(ProductImage.builder().url("https://picsum.photos/id/1084/800/800").product(erkekGomlek).build());
        addVariants(erkekGomlek, List.of("Kırmızı", "Mavi"), List.of("M", "L", "XL"), 60);
        productRepository.save(erkekGomlek);

        Product kadinSort = Product.builder()
                .name("Kadın Yüksek Bel Püsküllü Açık Mavi Jean Şort")
                .description("Kaliteli denim kumaştan üretilmiştir.")
                .price(new BigDecimal("550.00")) // Fiyatı daha mantıklı bir değere güncelledim.
                .category(kadinPantalonCategory) // Kategoriyi "Pantalon" olarak düzelttim.
                .active(true) // <<< DÜZELTME
                .build();
        kadinSort.getImages().add(ProductImage.builder().url("https://picsum.photos/id/1011/800/800").product(kadinSort).build());
        addVariants(kadinSort, List.of("Açık Mavi"), List.of("S", "M", "L", "XL"), 80); // Stok miktarını artırdım.
        productRepository.save(kadinSort);


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