package com.desafio.fastcommerce.application.service;

import com.desafio.fastcommerce.domain.DTOs.UserDto.ChangeRoleDTO;
import com.desafio.fastcommerce.domain.DTOs.UserDto.CreateUserDto;
import com.desafio.fastcommerce.domain.DTOs.UserDto.UpdateUserDto;
import com.desafio.fastcommerce.domain.DTOs.UserDto.UserResponseDto;
import com.desafio.fastcommerce.domain.entities.User;
import com.desafio.fastcommerce.domain.enums.Role;
import com.desafio.fastcommerce.domain.repository.UserRepository;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import com.desafio.fastcommerce.infrastructure.security.SecurityConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecurityConfig confi;

    public UserService(UserRepository userRepository, SecurityConfig confi) {
        this.userRepository= userRepository;
        this.confi = confi;
    }
    
    public Optional<UserResponseDto> getUserById(UUID id){
        return userRepository.findById(id)
                .map(this::toDto);
    }
    public Page<UserResponseDto> getUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(this::toDto);
    }
    public Page<UserResponseDto> getActiveUsers(Pageable pageable){
        return userRepository.findByIsActiveTrue(pageable).map(this::toDto);
    }

    @Transactional
    public void deactivateUser(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Usuário não encontrado"));
        long admins = userRepository.countByRole(Role.ADMIN);
        if (admins == 1 && user.getRole() == Role.ADMIN) {
            throw new CustomException("O sistema deve possuir ao menos um ADMIN");
        }
        user.setActive(false);
        userRepository.save(user);
    }
    @Transactional
    public void activateUser(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Usuário não encontrado"));
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void changeRole(UUID userId, ChangeRoleDTO dto){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Usuário não encontrado"));

        String authenticatedEmail =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        User authenticatedUser = userRepository.getUserByEmail(authenticatedEmail)
                .orElseThrow(() -> new CustomException("Usuário autenticado não encontrado"));

        long admins = userRepository.countByRole(Role.ADMIN);

        if (authenticatedUser.getId().equals(user.getId())
                && user.getRole() == Role.ADMIN
                && dto.role() != Role.ADMIN
                && admins == 1) {

            throw new CustomException(
                    "Você não pode remover o último ADMIN do sistema"
            );
        }

        user.setRole(dto.role());
        userRepository.save(user);
    }

    public UUID updateUser(UUID id,UpdateUserDto dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found"));
        Optional<User> userEmail = userRepository.getUserByEmail(dto.email());
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
        return userRepository.save(user).getId();
    }
    private UserResponseDto toDto(User user){
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
