package com.alerthub.security.service.impl;

import com.alerthub.security.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        // inject test values instead of @Value
        jwtService.getClass()
                .getDeclaredFields();
        // reflection because fields are private & @Value in prod
        TestReflection.setField(jwtService, "secret",
                "testSecretKeyForJwtService1234567890");
        TestReflection.setField(jwtService, "expirationMinutes", 60L);
    }

    @Test
    void generateAndValidateToken() {
        User userDetails = new User(
                "admin",
                "dummy",
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );

        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);
        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertThat(username).isEqualTo("admin");
        assertThat(valid).isTrue();
    }
}


/**
 * Small helper for setting private fields in tests.
 */
class TestReflection {
    static void setField(Object target, String name, Object value) {
        try {
            var field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
