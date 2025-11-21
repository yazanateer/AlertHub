package com.alerthub.user.service.impl;

import com.alerthub.user.dto.UserResponseDto;
import com.alerthub.user.exceptions.ResourceNotFoundException;
import com.alerthub.user.model.UserEntity;
import com.alerthub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers_shouldReturnDtos() {
        UserEntity u1 = UserEntity.builder()
                .id(1L).username("admin").email("a@x.com").phone("050").password("pw").build();
        UserEntity u2 = UserEntity.builder()
                .id(2L).username("ameen").email("b@x.com").phone("051").password("pw").build();

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UserResponseDto> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).username()).isEqualTo("admin");
    }

    @Test
    void getUserById_whenNotFound_shouldThrow() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(100L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
