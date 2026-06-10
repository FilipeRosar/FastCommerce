package com.desafio.fastcommerce.domain.DTOs.ReviewDTOs;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductReviewResponseDTO(
        UUID id,
        UUID userId,
        String userName,
        Integer score,
        String comment,
        LocalDateTime createdAt
) {
}
