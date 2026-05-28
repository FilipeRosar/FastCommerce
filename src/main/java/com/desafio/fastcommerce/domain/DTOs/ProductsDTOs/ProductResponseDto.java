package com.desafio.fastcommerce.domain.DTOs.ProductsDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDto(
        String nome,
        BigDecimal preco,
        String categoria,
        @CreationTimestamp
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime createdAt
) implements Serializable {
        private static final long serialVersionUID = 1L;
}
