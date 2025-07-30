package com.tandogan.ecommerce_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String gender;
    private String formattedName;
    private Long parentId; // YENİ EKLENEN ALAN
}