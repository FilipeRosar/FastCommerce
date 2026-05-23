package com.desafio.fastcommerce.domain.repository;

import com.desafio.fastcommerce.domain.entities.RefreshToken;
import com.desafio.fastcommerce.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
