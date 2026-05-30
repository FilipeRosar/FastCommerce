package com.desafio.fastcommerce.application.service;

import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrdersByStatusDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrdersDashboardResponseDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.TopSellingProductDTO;
import com.desafio.fastcommerce.domain.enums.OrderStatus;
import com.desafio.fastcommerce.domain.repository.OrderItemRepository;
import com.desafio.fastcommerce.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void deveRetornarDashboardComDadosCorretos() {

        LocalDateTime start = LocalDateTime.now().minusDays(30);
        LocalDateTime end = LocalDateTime.now();

        List<OrdersByStatusDTO> ordersByStatus = List.of(
                new OrdersByStatusDTO(OrderStatus.PENDING, 5L),
                new OrdersByStatusDTO(OrderStatus.CONFIRMED, 10L),
                new OrdersByStatusDTO(OrderStatus.DELIVERED, 15L)
        );

        Page<TopSellingProductDTO> topProducts = new PageImpl<>(
                List.of(
                        new TopSellingProductDTO(
                                UUID.randomUUID(),
                                "Notebook Gamer",
                                20L
                        ),
                        new TopSellingProductDTO(
                                UUID.randomUUID(),
                                "Mouse Gamer",
                                15L
                        )
                )
        );

        BigDecimal revenue = BigDecimal.valueOf(15000);

        when(orderRepository.countOrdersByStatus(start, end))
                .thenReturn(ordersByStatus);

        when(orderRepository.getTotalRevenue(start, end))
                .thenReturn(revenue);

        when(orderItemRepository.findTopSellingProducts(
                eq(start),
                eq(end),
                any(Pageable.class)
        )).thenReturn(topProducts);

        OrdersDashboardResponseDTO response =
                reportService.getDashboard(start, end);

        assertNotNull(response);
        assertEquals(
                3,
                response.ordersByStatus().size()
        );
        assertEquals(
                2,
                response.topProducts().size()
        );
        assertEquals(
                revenue,
                response.totalRevenue()
        );

        assertEquals(
                OrderStatus.DELIVERED,
                response.ordersByStatus().get(2).status()
        );

        assertEquals(
                "Notebook Gamer",
                response.topProducts().get(0).productName()
        );

        verify(orderRepository)
                .countOrdersByStatus(start, end);

        verify(orderRepository)
                .getTotalRevenue(start, end);

        verify(orderItemRepository)
                .findTopSellingProducts(
                        eq(start),
                        eq(end),
                        any(Pageable.class)
                );
    }
}