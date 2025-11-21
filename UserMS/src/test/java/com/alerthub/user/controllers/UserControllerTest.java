package com.alerthub.user.controllers;

import com.alerthub.user.config.JwtAuthenticationFilter;
import com.alerthub.user.dto.UserResponseDto;
import com.alerthub.user.service.CustomUserDetailsService;
import com.alerthub.user.service.JwtService;
import com.alerthub.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockitoBean 
    private UserService userService;
    
    @MockitoBean 
    private JwtService jwtService;       
    
    @MockitoBean 
    private CustomUserDetailsService uds;
    
    @MockitoBean 
    private JwtAuthenticationFilter jwtFilter;

    @Test
    void getAll_shouldReturnUsers() throws Exception {
        List<UserResponseDto> list = List.of(
                new UserResponseDto(1L, "admin", "admin@x.com", "050"),
                new UserResponseDto(2L, "ameen", "ameen@x.com", "051")
        );

        Mockito.when(userService.getAllUsers()).thenReturn(list);

        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("admin"));
    }

    @Test
    void getById_shouldReturnUser() throws Exception {
        UserResponseDto dto = new UserResponseDto(1L, "admin", "admin@x.com", "050");
        Mockito.when(userService.getUserById(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }
}
