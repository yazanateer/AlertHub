package com.alerthub.loader.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alerthub.loader.service.JwtService;
import com.alerthub.loader.dto.ScanResult;
import com.alerthub.loader.model.PlatformInformation;
import com.alerthub.loader.service.LoaderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/loader")
@RequiredArgsConstructor
public class LoaderController {

	private final LoaderService loaderService;
	private final JwtService jwtService;

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        return authHeader.substring(7);
    }

    private String getManagerEmailFromJwt(String token) {
        String email = jwtService.extractEmail(token);
        // fallback to username if email is not present
        return (email != null && !email.isBlank())
                ? email
                : jwtService.extractUsername(token);
    }

    private String getManagerPhoneFromJwt(String token) {
        // assumes JWT includes a "phone" claim
        try {
            String phone = jwtService.extractPhone(token);
            return (phone != null && !phone.isBlank()) ? phone : null;
        } catch (Exception e) {
            return null;
        }
    }
	
	@PostMapping("scan")
	@PreAuthorize("hasRole('triggerProcess')")
	public ScanResult scan() {
		return loaderService.scan();
	}
	
	@GetMapping("/hello")
    public String sayHello() {
        return "Hello from LoaderMS";
    }
	
	@GetMapping("/platform-information")
    public List<PlatformInformation> getAll() {
        return loaderService.getAll();
    }
}
