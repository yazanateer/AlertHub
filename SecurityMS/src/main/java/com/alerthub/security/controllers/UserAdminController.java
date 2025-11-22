package com.alerthub.security.controllers;

import com.alerthub.security.dto.CreateUserRequestDto;
import com.alerthub.security.dto.UserResponseDto;
import com.alerthub.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserRequestDto dto) {
        return userService.createUser(dto);
    }

    @PostMapping("/{userId}/roles/{roleName}")
    public UserResponseDto assignRole(
            @PathVariable Long userId,
            @PathVariable String roleName
    ) {
        return userService.assignRole(userId, roleName);
    }
}
