package com.desafio.fastcommerce.domain.DTOs.RatingDTOs;

import java.time.LocalDateTime;
import java.util.UUID;

public record RatingResponseDTO(
        UUID id,
        UUID userId,
        String userName,
        Integer score,
        String comment,
        LocalDateTime createdAt
) {
}
