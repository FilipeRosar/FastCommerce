package com.desafio.fastcommerce.domain.DTOs.RatingDTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateRatingDTO(
        @NotNull
        UUID productId,
        @Min(1)
        @Max(5)
        Integer score,
        String comment
) {
}
