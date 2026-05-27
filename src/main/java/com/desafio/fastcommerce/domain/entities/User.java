package com.desafio.fastcommerce.domain.entities;

import com.desafio.fastcommerce.domain.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String email;
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private String phoneNumber;
    private String cpf;
    private boolean isActive;

    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date updatedAt;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Orders> orders = new ArrayList<>();
}
