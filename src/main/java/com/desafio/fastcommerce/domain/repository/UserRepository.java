package com.desafio.fastcommerce.domain.repository;

import com.desafio.fastcommerce.domain.entities.User;
import com.desafio.fastcommerce.domain.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByEmail(String email);
    long countByRole(Role role);
    Page<User> findByIsActiveTrue(Pageable pageable);
}
