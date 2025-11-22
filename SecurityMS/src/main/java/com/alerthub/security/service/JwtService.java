package com.alerthub.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails details);

    String generateToken(UserDetails details);
}
