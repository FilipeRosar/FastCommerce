package com.desafio.fastcommerce.domain.DTOs.cartDTOs;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponseDTO(
        UUID cartId,
        UUID userId,
        BigDecimal totalAmount,
        List<CartItemResponseDTO> items
) {
}
