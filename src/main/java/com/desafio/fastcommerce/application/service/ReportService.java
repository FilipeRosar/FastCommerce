package com.desafio.fastcommerce.application.service;


import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrdersByStatusDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrdersDashboardResponseDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.TopSellingProductDTO;
import com.desafio.fastcommerce.domain.enums.OrderStatus;
import com.desafio.fastcommerce.domain.repository.OrderItemRepository;
import com.desafio.fastcommerce.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrdersDashboardResponseDTO getDashboard(LocalDateTime startDate, LocalDateTime endDate) {
        var ordersByStatus =
                orderRepository.countOrdersByStatus(startDate, endDate);

        var revenue = orderRepository.getTotalRevenue(startDate, endDate);

        var topProducts = orderItemRepository.findTopSellingProducts(
                startDate,
                endDate,
                PageRequest.of(0, 10)
        ).getContent();

        return new OrdersDashboardResponseDTO(
                ordersByStatus,
                topProducts,
                revenue
        );
    }
}
