package com.desafio.fastcommerce.domain.DTOs.ProductsDTOs;

import java.math.BigDecimal;

public record UpdateProductsDto(
        String name,
        String descricao,
        BigDecimal preco,
        Integer estoque,
        String categoria
) {
}
