package com.alerthub.security.dto;

import java.util.List;

public record CreateUserRequestDto(
        String username,
        String email,
        String phone,
        String password,
        List<Long> roleIds
) {
	
}
