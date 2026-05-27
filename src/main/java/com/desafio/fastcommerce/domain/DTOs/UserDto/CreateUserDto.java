package com.desafio.fastcommerce.domain.DTOs.UserDto;

import jakarta.validation.constraints.*;

public record CreateUserDto(
        @Size(min = 3, message = "Nome deve ter no minimo 3 caracteres")
        @NotBlank(message = "Nome não pode estar vazio")
        String name,
        @Email(message = "Email invalido")
        @NotNull(message = "Email não pode estar vazio")
        String email,
        @NotNull(message = "Senha não pode ficar vazia")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "A senha deve ter no minimo 8 caracteres, com letras e numeros."
        )
        String passwordHash,
        String cpf,
        String phoneNumber
                            ) {
}
