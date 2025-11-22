package com.alerthub.security.service;

import com.alerthub.security.dto.LoginRequestDto;
import com.alerthub.security.dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto dto);
}
