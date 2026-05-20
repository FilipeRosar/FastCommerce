package com.desafio.fastcommerce.domain.entities;

import com.desafio.fastcommerce.domain.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "tb_user")
public class User {
    @Id
    private UUID id;
    private String name;
    
    private String email;
    private String passwordHash;
    private Role role;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;


}
