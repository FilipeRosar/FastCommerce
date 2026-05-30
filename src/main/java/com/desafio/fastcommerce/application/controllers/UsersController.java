package com.desafio.fastcommerce.application.controllers;


import com.desafio.fastcommerce.application.service.UserService;
import com.desafio.fastcommerce.domain.DTOs.UserDto.ChangeRoleDTO;
import com.desafio.fastcommerce.domain.DTOs.UserDto.UpdateUserDto;
import com.desafio.fastcommerce.domain.DTOs.UserDto.UserResponseDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    public ResponseEntity<Page<UserResponseDto>> getUsers(Pageable pageable){
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDto>> getActiveUsers(Pageable pageable){
        return ResponseEntity.ok(userService.getActiveUsers(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id){
        return userService.getUserById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UUID> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserDto dto
    ) {
        return ResponseEntity.ok(
                userService.updateUser(id, dto)
        );
    }
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeRole(
            @PathVariable UUID id,
            @RequestBody @Valid ChangeRoleDTO dto
    ) {
        userService.changeRole(id, dto);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable UUID id
    ) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateUser(
            @PathVariable UUID id
    ) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }
}
