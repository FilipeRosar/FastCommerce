package com.desafio.fastcommerce.domain.DTOs.ordersDTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequestDTO(

        @NotEmpty
        List<@Valid OrderItemRequestDTO> items
) {
}
