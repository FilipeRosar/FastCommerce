package com.desafio.fastcommerce.application.controllers;


import com.desafio.fastcommerce.application.service.UserService;
import com.desafio.fastcommerce.domain.DTOs.UserDto.ChangeRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeRole(
            @PathVariable UUID id,
            @RequestBody ChangeRoleDTO dto
            ){
        userService.changeRole(id, dto);
        return ResponseEntity.ok().build();
    }

}
