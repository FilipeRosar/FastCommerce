package com.desafio.fastcommerce.domain.DTOs.ordersDTOs;

import java.math.BigDecimal;
import java.util.List;

public record OrdersDashboardResponseDTO(
        List<OrdersByStatusDTO> ordersByStatus,
        List<TopSellingProductDTO> topProducts,
        BigDecimal totalRevenue
) {
}
