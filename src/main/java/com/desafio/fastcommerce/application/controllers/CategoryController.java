package com.desafio.fastcommerce.application.controllers;

import com.desafio.fastcommerce.domain.DTOs.CategoryResponseDTO;
import com.desafio.fastcommerce.domain.enums.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @GetMapping
    public List<CategoryResponseDTO> getCategories() {
        return Arrays.stream(Category.values())
                .map(category -> new CategoryResponseDTO(
                        category.name(),
                        category.getDescription()
                ))
                .toList();
    }
}