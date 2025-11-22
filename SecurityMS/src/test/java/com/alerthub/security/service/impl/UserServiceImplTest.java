package com.alerthub.security.service.impl;

import com.alerthub.security.dto.CreateUserRequestDto;
import com.alerthub.security.dto.UserResponseDto;
import com.alerthub.security.exceptions.ResourceNotFoundException;
import com.alerthub.security.model.RoleEntity;
import com.alerthub.security.model.UserEntity;
import com.alerthub.security.repository.RoleRepository;
import com.alerthub.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleRepository roleRepo;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldReturnDtoWithRoles() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "Ameen",
                "ameen@example.com",
                "0501234567",
                "secret",
                List.of(1L, 2L)
        );

        RoleEntity r1 = RoleEntity.builder().id(1L).name("createAction").build();
        RoleEntity r2 = RoleEntity.builder().id(2L).name("updateAction").build();

        when(roleRepo.findAllById(dto.roleIds())).thenReturn(List.of(r1, r2));
        when(encoder.encode("secret")).thenReturn("encoded");
        when(userRepo.save(any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity u = invocation.getArgument(0);
                    u.setId(100L);
                    return u;
                });

        UserResponseDto result = userService.createUser(dto);

        assertThat(result.id()).isEqualTo(100L);
        assertThat(result.username()).isEqualTo("Ameen");
        assertThat(result.roles()).containsExactlyInAnyOrder("createAction", "updateAction");
        verify(userRepo, times(1)).save(any(UserEntity.class));
    }

    @Test
    void assignRole_whenUserOrRoleMissing_shouldThrow() {
        when(userRepo.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.assignRole(999L, "ADMIN"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void assignRole_shouldAddRoleAndReturnUpdatedUser() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("admin")
                .email("admin@x.com")
                .password("pw")
                .roles(Set.of())
                .build();

        RoleEntity adminRole = RoleEntity.builder()
                .id(10L).name("ADMIN").build();

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepo.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        when(userRepo.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        UserResponseDto dto = userService.assignRole(1L, "ADMIN");

        assertThat(dto.roles()).contains("ADMIN");
    }
}
