package com.desafio.fastcommerce.domain.DTOs.UserDto;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String email,
        String name
) {
}
