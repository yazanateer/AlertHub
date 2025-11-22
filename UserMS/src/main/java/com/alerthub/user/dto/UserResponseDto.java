package com.alerthub.user.dto;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        String phone
) {}
