package com.desafio.fastcommerce.domain.DTOs.events;

import com.desafio.fastcommerce.domain.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderEventDTO(
        UUID orderId,
        OrderStatus status,
        BigDecimal totalAmount,
        @JsonFormat(pattern = "dd/MM/yyyy hh:mm")
        @CreationTimestamp
        LocalDateTime createdAt
) {
}
