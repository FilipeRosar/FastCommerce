package com.desafio.fastcommerce.domain.repository;

import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrdersByStatusDTO;
import com.desafio.fastcommerce.domain.entities.Orders;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID> {

    @Query("""
        SELECT o FROM Orders o LEFT JOIN FETCH o.items WHERE o.id = :id
    """)
    Optional<Orders> findByIdWithItems(@Param("id") UUID id);

    @Query("""
    SELECT o.status, COUNT(o)
    FROM Orders o
    WHERE o.createdAt BETWEEN :startDate AND :endDate
    GROUP BY o.status
    """)
    List<OrdersByStatusDTO> countOrdersByStatus(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    @Query("""
    SELECT COALESCE(SUM(o.totalAmount),0)
    FROM Orders o
    WHERE o.status = 'DELIVERED'
    AND o.createdAt BETWEEN :startDate AND :endDate
    """)
    BigDecimal getTotalRevenue(@Param("startDate") LocalDateTime statDate,
                               @Param("endDate") LocalDateTime endDate
    );
}
