package com.alerthub.evaluation.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

public interface JwtService {

    String extractUsername(String token);

    Long extractUserId(String token);

    String extractEmail(String token);
    
    String extractPhone(String phone);

    Collection<? extends GrantedAuthority> extractAuthorities(String token);

    boolean isTokenValid(String token);
}
