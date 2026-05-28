package com.desafio.fastcommerce.domain.DTOs.ordersDTOs;

import com.desafio.fastcommerce.domain.entities.OrderItems;
import com.desafio.fastcommerce.domain.entities.Orders;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
    public static OrderItemResponseDTO fromEntity(OrderItems items) {
        return new OrderItemResponseDTO(
          items.getProduct().getId(),
                items.getProduct().getName(),
                items.getQuantity(),
                items.getUnitPrice(),
                items.getSubTotal()
        );
    }
}
