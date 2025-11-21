package com.alerthub.security.service.impl;

import com.alerthub.security.dto.CreateUserRequestDto;
import com.alerthub.security.dto.UserResponseDto;
import com.alerthub.security.exceptions.ResourceNotFoundException;
import com.alerthub.security.model.RoleEntity;
import com.alerthub.security.model.UserEntity;
import com.alerthub.security.repository.RoleRepository;
import com.alerthub.security.repository.UserRepository;
import com.alerthub.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    @Override
    public UserResponseDto createUser(CreateUserRequestDto dto) {

        var roles = new HashSet<>(roleRepo.findAllById(dto.roleIds()));

        UserEntity user = UserEntity.builder()
                .username(dto.username())
                .email(dto.email())
                .phone(dto.phone())
                .password(encoder.encode(dto.password()))
                .roles(roles)
                .build();

        UserEntity saved = userRepo.save(user);
        return toDto(saved);
    }

    @Override
    public UserResponseDto assignRole(Long userId, String roleName) {

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        RoleEntity role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.getRoles().add(role);
        UserEntity updated = userRepo.save(user);

        return toDto(updated);
    }

    private UserResponseDto toDto(UserEntity user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles().stream().map(RoleEntity::getName).toList()
        );
    }
}
