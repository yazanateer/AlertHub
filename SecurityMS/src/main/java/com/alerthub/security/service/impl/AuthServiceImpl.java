package com.alerthub.security.service.impl;

import com.alerthub.security.dto.LoginRequestDto;
import com.alerthub.security.dto.LoginResponseDto;
import com.alerthub.security.service.AuthService;
import com.alerthub.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );

        UserDetails user = userDetailsService.loadUserByUsername(dto.username());
        String token = jwtService.generateToken(user);

        return new LoginResponseDto(
                token,
                user.getUsername(),
                user.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .toList()
        );
    }
}
