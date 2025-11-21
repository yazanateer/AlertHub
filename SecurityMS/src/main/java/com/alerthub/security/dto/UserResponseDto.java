package com.alerthub.security.dto;

import java.util.List;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        String phone,
        List<String> roles
) {
	
}
