package com.desafio.fastcommerce.domain.DTOs.ProductsDTOs;

import com.desafio.fastcommerce.domain.enums.Category;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProdutsDto(
        @NotNull
        String nome,
        String descricao,
        @NotNull
        BigDecimal preco,
        @NotNull
        Integer estoque,
        Category categoria
) {
}
