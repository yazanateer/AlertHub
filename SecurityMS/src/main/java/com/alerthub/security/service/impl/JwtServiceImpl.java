package com.alerthub.security.service.impl;

import com.alerthub.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.alerthub.security.model.UserEntity;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    // ----------- EXTRACTION ----------- //

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long extractUserId(String token) {
        Object value = extractClaim(token, claims -> claims.get("userId"));
        return value == null ? null : Long.valueOf(value.toString());
    }

    @Override
    public String extractEmail(String token) {
        Object value = extractClaim(token, claims -> claims.get("email"));
        return value == null ? null : value.toString();
    }

    @Override
    public List<String> extractRoles(String token) {
        Object roles = extractClaim(token, claims -> claims.get("roles"));
        return (roles instanceof List<?> list)
                ? list.stream().map(Object::toString).toList()
                : List.of();
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }

    // ----------- VALIDATION ----------- //

    @Override
    public boolean isTokenValid(String token, UserDetails details) {
        try {
            String username = extractUsername(token);
            return username.equals(details.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration != null && expiration.before(new Date());
    }

    // ----------- GENERATION ----------- //

    @Override
    public String generateToken(UserDetails details) {
       
        UserEntity user = (UserEntity) details;

        long now = System.currentTimeMillis();

        Map<String, Object> claims = Map.of(
                "userId", user.getId(),
                "email", user.getEmail(),
                "roles", details.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .toList()
        );

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(details.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMinutes * 60000))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
