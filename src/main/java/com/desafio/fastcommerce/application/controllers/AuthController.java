package com.desafio.fastcommerce.application.controllers;

import com.desafio.fastcommerce.domain.DTOs.AuthDTOs.AuthResponseDto;
import com.desafio.fastcommerce.domain.DTOs.AuthDTOs.LoginRequestDto;
import com.desafio.fastcommerce.domain.DTOs.AuthDTOs.RefreshTokenRequestDto;
import com.desafio.fastcommerce.domain.DTOs.UserDto.CreateUserDto;
import com.desafio.fastcommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CreateUserDto> register(@RequestBody @Valid CreateUserDto dto) throws Exception {
        var userId = authService.register(dto);
        return ResponseEntity.created(URI.create("v1/user/"+userId.toString())).build();
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto dto) throws Exception {
        var response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody @Valid RefreshTokenRequestDto dto) throws Exception {
        AuthResponseDto responseDto = authService.refreshToken(dto);
        return ResponseEntity.ok(responseDto);
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequestDto dto) throws Exception {
        authService.logout(dto.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
