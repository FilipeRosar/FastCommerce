package com.desafio.fastcommerce.domain.DTOs.ProductsDTOs;

import com.desafio.fastcommerce.domain.enums.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String nome,
        BigDecimal preco,
        Category categoria,
        String descricao,
        Double averageRating,
        Integer totalRatings,

        @CreationTimestamp
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime createdAt
) implements Serializable {
        private static final long serialVersionUID = 1L;
}
