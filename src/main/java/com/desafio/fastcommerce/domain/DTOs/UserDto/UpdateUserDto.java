package com.desafio.fastcommerce.domain.DTOs.UserDto;

public record UpdateUserDto(
        String nome,
        String password,
        String cpf,
        String email,
        String phoneNumber
) {
}
