package com.alerthub.actionsjobsscheduler.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public interface JwtService {
	String extractUsername(String token);

    Long extractUserId(String token);

    String extractEmail(String token);
    
    String extractPhone(String phone);

    Collection<? extends GrantedAuthority> extractAuthorities(String token);

    boolean isTokenValid(String token);
}

