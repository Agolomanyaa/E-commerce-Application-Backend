package com.tandogan.ecommerce_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Bu sınıf, Spring Boot'un otomatik olarak yapması beklenen ancak bir şekilde
 * başarısız olan @Transactional desteğini, manuel olarak ve zorla etkinleştirmek için
 * oluşturulmuştur. Bu, projedeki "hayalet güncelleme" sorununa karşı son çaredir.
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
}