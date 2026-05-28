package com.desafio.fastcommerce.domain.repository;

import com.desafio.fastcommerce.domain.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID> {

}
