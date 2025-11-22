package com.alerthub.security.dto;

import java.util.List;

public record LoginResponseDto(String token, String username, List<String> roles) {
	
}
