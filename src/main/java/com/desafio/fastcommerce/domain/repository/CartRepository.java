package com.desafio.fastcommerce.domain.repository;

import com.desafio.fastcommerce.domain.entities.Cart;
import com.desafio.fastcommerce.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser(User user);
}
