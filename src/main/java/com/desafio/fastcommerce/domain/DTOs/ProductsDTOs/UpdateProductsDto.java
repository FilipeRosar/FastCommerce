package com.desafio.fastcommerce.domain.DTOs.ProductsDTOs;

import com.desafio.fastcommerce.domain.enums.Category;

import java.math.BigDecimal;

public record UpdateProductsDto(
        String name,
        String descricao,
        BigDecimal preco,
        Integer estoque,
        Category categoria
) {
}
