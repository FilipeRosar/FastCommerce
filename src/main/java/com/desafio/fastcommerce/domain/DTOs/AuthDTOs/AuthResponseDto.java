package com.desafio.fastcommerce.domain.DTOs.AuthDTOs;

import java.util.UUID;

public record AuthResponseDto(
        String accessToken,
        String refreshToken,
        Long expiresIn,

        UUID id,
        String name,
        String email,
        String role
) {
}
