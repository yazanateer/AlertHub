package com.alerthub.metric.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alerthub.metric.service.JwtService;
import com.alerthub.metric.dto.MetricCreateRequest;
import com.alerthub.metric.dto.MetricResponse;
import com.alerthub.metric.dto.MetricUpdateRequest;
import com.alerthub.metric.service.MetricService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/metrics")
@Tag(name = "Metrics", description = "Metric management APIs")
public class MetricController {

	private final MetricService metricService;
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
    
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new metric")
	@PreAuthorize("hasRole('createMetric')")
	public MetricResponse createMetric(@Valid @RequestBody MetricCreateRequest request) {
		return metricService.createMetric(request);
	}
	
	@GetMapping
    @Operation(summary = "Get all metrics for a user")
	@PreAuthorize("isAuthenticated()")
	public List<MetricResponse> getMetricByUser(@RequestParam int userId){
		return metricService.getMetricByUser(userId);
	}
	
	@GetMapping("/{id}")
    @Operation(summary = "Get a metric by id for a user")
	@PreAuthorize("isAuthenticated()")
	public MetricResponse getMetricById(@PathVariable Long id,
			@RequestParam int userId) {
        return metricService.getMetricById(id, userId);
	}
	
	@PutMapping("/{id}")
    @Operation(summary = "Update a metric by id")
	@PreAuthorize("hasRole('updateMetric')")
	public MetricResponse updateMetric(@PathVariable Long id,
			@RequestParam int userId,
			@Valid @RequestBody MetricUpdateRequest request) {
        return metricService.updateMetric(id, userId, request);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a metric by id")
	@PreAuthorize("hasRole('deleteMetric')")
	public void deleteMetric(@PathVariable Long id,
			@RequestParam int userId) {
		metricService.deleteMetric(id, userId);
	}
}
