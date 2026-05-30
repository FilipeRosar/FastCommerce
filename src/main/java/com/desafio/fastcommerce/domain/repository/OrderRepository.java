package com.desafio.fastcommerce.domain.repository;

import com.desafio.fastcommerce.domain.entities.Orders;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID> {

    @Query("""
        SELECT o FROM Orders o LEFT JOIN FETCH o.items WHERE o.id = :id
    """)
    Optional<Orders> findByIdWithItems(@Param("id") UUID id);
}
