package com.alerthub.user.service;

import com.alerthub.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);
}
