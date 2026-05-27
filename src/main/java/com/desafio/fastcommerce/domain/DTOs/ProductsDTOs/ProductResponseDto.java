package com.desafio.fastcommerce.domain.DTOs.ProductsDTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDto(
        String nome,
        BigDecimal preco,
        String categoria,
        LocalDateTime createdAt
) {
}
