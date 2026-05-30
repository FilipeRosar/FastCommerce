package com.desafio.fastcommerce.domain.repository;


import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.TopSellingProductDTO;
import com.desafio.fastcommerce.domain.entities.OrderItems;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, UUID> {


    @Query("""
    SELECT 
        oi.product.id,
        oi.product.name,
            SUM(oi.quantity)
                FROM OrderItems oi 
                JOIN oi.order o 
                WHERE o.createdAt BETWEEN :startDate AND :endDate
                GROUP BY oi.product.id, oi.product.name
                ORDER BY SUM(oi.quantity) DESC 
                        
    """)
    Page<TopSellingProductDTO> findTopSellingProducts(@Param("startDate")LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate,
                                                      Pageable pageable);
}
