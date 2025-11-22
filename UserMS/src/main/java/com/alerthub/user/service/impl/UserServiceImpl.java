package com.alerthub.user.service.impl;

import com.alerthub.user.dto.UserResponseDto;
import com.alerthub.user.exceptions.ResourceNotFoundException;
import com.alerthub.user.model.UserEntity;
import com.alerthub.user.repository.UserRepository;
import com.alerthub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toDto(user);
    }

    private UserResponseDto toDto(UserEntity u) {
        return new UserResponseDto(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getPhone()
        );
    }
}
