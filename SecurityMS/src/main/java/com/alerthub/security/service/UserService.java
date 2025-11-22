package com.alerthub.security.service;

import com.alerthub.security.dto.CreateUserRequestDto;
import com.alerthub.security.dto.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(CreateUserRequestDto dto);

    UserResponseDto assignRole(Long userId, String roleName);
}
