package com.alerthub.security.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUsername(String token);

    public Long extractUserId(String token);

    public String extractEmail(String token);
    
    public List<String> extractRoles(String token);

    boolean isTokenValid(String token, UserDetails details);

    String generateToken(UserDetails details);
}
