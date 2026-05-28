package com.desafio.fastcommerce.domain.DTOs.ordersDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItemRequestDTO(
        @NotNull
        UUID productId,

        @NotNull
        @Min(1)
        Integer quantity
) {
}
