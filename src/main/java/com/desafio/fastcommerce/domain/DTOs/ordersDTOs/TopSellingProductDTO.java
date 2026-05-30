package com.desafio.fastcommerce.domain.DTOs.ordersDTOs;

import java.util.UUID;

public record TopSellingProductDTO(
        UUID productId,
        String productName,
        Long totalSold
) {
}
