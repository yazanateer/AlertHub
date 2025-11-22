package com.alerthub.user.controllers;

import com.alerthub.user.dto.UserResponseDto;
import com.alerthub.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public List<UserResponseDto> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public UserResponseDto getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
