package com.desafio.fastcommerce.application.service;


import com.desafio.fastcommerce.domain.DTOs.AuthDTOs.AuthResponseDto;
import com.desafio.fastcommerce.domain.DTOs.AuthDTOs.LoginRequestDto;
import com.desafio.fastcommerce.domain.DTOs.AuthDTOs.RefreshTokenRequestDto;
import com.desafio.fastcommerce.domain.DTOs.UserDto.CreateUserDto;
import com.desafio.fastcommerce.domain.entities.RefreshToken;
import com.desafio.fastcommerce.domain.entities.User;
import com.desafio.fastcommerce.domain.enums.Role;
import com.desafio.fastcommerce.domain.repository.RefreshTokenRepository;
import com.desafio.fastcommerce.domain.repository.UserRepository;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import com.desafio.fastcommerce.infrastructure.security.SecurityConfig;
import com.desafio.fastcommerce.infrastructure.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UUID register(CreateUserDto dto){
        if (userRepository.getUserByEmail(dto.email()).isPresent()){
            throw new CustomException("Email já cadastrado");
        }
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setCpf(dto.cpf());
        user.setRole(Role.COSTUMER);
        user.setCreatedAt(LocalDateTime.now());
        user.setPhoneNumber(dto.phoneNumber());
        user.setActive(true);
        user.setPasswordHash(
                passwordEncoder.encode(dto.passwordHash())
        );
        return userRepository.save(user).getId();
    }
    @Transactional
    public AuthResponseDto login(LoginRequestDto dto){
        User user = userRepository.getUserByEmail(dto.email())
                .orElseThrow(() -> new CustomException("Email ou senha inválidos"));

        boolean passwordMatches = passwordEncoder.matches(
                dto.password(),
                user.getPasswordHash()
        );
        if (!passwordMatches) {
            throw new CustomException("Email ou senha inválidos");
        }
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);
        refreshTokenRepository.flush();

        String accessToken = jwtService.generateToken(user);
        String refreshTokenValues = jwtService.generateRefreshToken(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenValues);
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);
        refreshToken.setExpiration(Instant.now().plusSeconds(604800));
        refreshTokenRepository.save(refreshToken);

        return new AuthResponseDto(
                accessToken,
                refreshTokenValues,
                900L,

                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
    public AuthResponseDto refreshToken(RefreshTokenRequestDto dto){
        RefreshToken oldRefreshToken = refreshTokenRepository.findByToken(dto.refreshToken())
                .orElseThrow(() -> new CustomException("Refresh token invalido"));
        if (oldRefreshToken.isRevoked()){
            throw new CustomException("Refresh token revogado");
        }
        if (oldRefreshToken.getExpiration().isBefore(Instant.now())){
            throw new CustomException("Refresh token expirada");
        }
        User user = oldRefreshToken.getUser();

        refreshTokenRepository.delete(oldRefreshToken);
        refreshTokenRepository.flush();

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshTokenValues = jwtService.generateRefreshToken(user);

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken(newRefreshTokenValues);
        newRefreshToken.setUser(user);
        newRefreshToken.setRevoked(false);
        newRefreshToken.setExpiration(Instant.now().plusSeconds(604800));
        refreshTokenRepository.save(newRefreshToken);

        return new AuthResponseDto(
                newAccessToken,
                newRefreshTokenValues,
                900L,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
    public void logout (String refreshTokenValue){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new CustomException("Refresh token invalido"));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
}
