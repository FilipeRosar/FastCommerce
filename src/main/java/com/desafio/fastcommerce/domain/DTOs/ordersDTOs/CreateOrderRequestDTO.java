package com.desafio.fastcommerce.domain.DTOs.ordersDTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequestDTO(

        @NotEmpty(message = "A lista de itens não pode estar vazia")
        @Valid
        List<OrderItemRequestDTO> items
) {
}
