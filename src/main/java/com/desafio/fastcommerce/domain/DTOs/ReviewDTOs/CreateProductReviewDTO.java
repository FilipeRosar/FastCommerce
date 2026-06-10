package com.desafio.fastcommerce.domain.DTOs.ReviewDTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateProductReviewDTO(
        @NotNull
        UUID productId,

        @NotNull
        @Min(1)
        @Max(5)
        Integer score,
        String comment
) {
}
