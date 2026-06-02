package com.desafio.fastcommerce.domain.DTOs.cartDTOs;

import java.util.UUID;

public record AddItemCartDTO(
        UUID productId,
        Integer quantity
) {
}
