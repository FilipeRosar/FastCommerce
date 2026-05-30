package com.desafio.fastcommerce.domain.DTOs.ordersDTOs;

import com.desafio.fastcommerce.domain.enums.OrderStatus;

public record OrdersByStatusDTO(
        OrderStatus status,
        Long total
) {
}
