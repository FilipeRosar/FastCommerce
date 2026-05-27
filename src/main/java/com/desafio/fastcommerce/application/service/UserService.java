package com.desafio.fastcommerce.application.service;

import com.desafio.fastcommerce.domain.DTOs.UserDto.CreateUserDto;
import com.desafio.fastcommerce.domain.DTOs.UserDto.UpdateUserDto;
import com.desafio.fastcommerce.domain.DTOs.UserDto.UserResponseDto;
import com.desafio.fastcommerce.domain.entities.User;
import com.desafio.fastcommerce.domain.enums.Role;
import com.desafio.fastcommerce.domain.repository.UserRepository;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import com.desafio.fastcommerce.infrastructure.security.SecurityConfig;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository _userRepository;
    private final SecurityConfig confi;

    public UserService(UserRepository _userRepository, SecurityConfig confi) {
        this._userRepository = _userRepository;
        this.confi = confi;
    }
    
    public Optional<UserResponseDto> getUserById(UUID id){
        return _userRepository.findById(id)
                .map(this::toDto);
    }
    public UUID updateUser(UUID id,UpdateUserDto dto){
        User user = _userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found"));
        Optional<User> userEmail = _userRepository.getUserByEmail(dto.email());
        if (userEmail.isPresent() && !user.getEmail().equals(dto.email())) {
            throw new CustomException("Email já existente");
        }
        var passwordEncoder = confi.encoder();
        user.setName(dto.nome());
        user.setEmail(dto.email());
        user.setCpf(dto.cpf());

        if (dto.password() != null && !dto.password().isBlank()){
            user.setPasswordHash(passwordEncoder.encode(dto.password()));
        }
        return _userRepository.save(user).getId();
    }
    private UserResponseDto toDto(User user){
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }
}
