package com.desafio.fastcommerce.domain.DTOs.UserDto;

import com.desafio.fastcommerce.domain.enums.Role;

public record ChangeRoleDTO(
        Role role
) {
}
