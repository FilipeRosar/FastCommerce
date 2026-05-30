package com.desafio.fastcommerce.domain.DTOs.ordersDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItemRequestDTO(
        @NotNull(message = "O Id do produto é obrigatório")
        UUID productId,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade minima permitida é 1")
        Integer quantity
) {
}
