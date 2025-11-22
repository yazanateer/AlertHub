package com.alerthub.security.service.impl;

import com.alerthub.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-minutes}")
    private long expirationMinutes;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails details) {
        return extractUsername(token).equals(details.getUsername());
    }

    @Override
    public String generateToken(UserDetails details) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(details.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMinutes * 60 * 1000))
                .claim("roles",
                        details.getAuthorities().stream()
                                .map(a -> a.getAuthority())
                                .toList()
                )
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
