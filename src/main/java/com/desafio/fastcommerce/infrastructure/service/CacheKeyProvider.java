package com.desafio.fastcommerce.infrastructure.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("cacheKey")
public class CacheKeyProvider {
    public String currentUser() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
