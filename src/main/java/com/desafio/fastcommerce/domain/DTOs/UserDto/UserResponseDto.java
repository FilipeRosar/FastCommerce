package com.desafio.fastcommerce.domain.DTOs.UserDto;

import com.desafio.fastcommerce.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String email,
        String name,
        String phoneNumber,
        Role role,
        boolean isActive,
        LocalDateTime createdAt
) {
}
