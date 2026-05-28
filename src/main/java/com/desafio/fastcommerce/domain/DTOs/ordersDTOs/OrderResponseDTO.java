package com.desafio.fastcommerce.domain.DTOs.ordersDTOs;

import com.desafio.fastcommerce.domain.entities.Orders;
import com.desafio.fastcommerce.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItemResponseDTO> items
) {
    public static OrderResponseDTO fromEntity(Orders order) {

        return new OrderResponseDTO(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),

                order.getItems()
                        .stream()
                        .map(OrderItemResponseDTO::fromEntity)
                        .toList()
        );
    }
}
