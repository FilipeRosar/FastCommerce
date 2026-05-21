package com.desafio.fastcommerce.domain.repository;

import com.desafio.fastcommerce.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByEmail(String email);
}
