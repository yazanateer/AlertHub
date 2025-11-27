package com.alerthub.actionsjobsscheduler.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alerthub.actionsjobsscheduler.dto.ActionCreateRequest;
import com.alerthub.actionsjobsscheduler.dto.ActionResponse;
import com.alerthub.actionsjobsscheduler.dto.ActionUpdateRequest;
import com.alerthub.actionsjobsscheduler.service.ActionService;
import com.alerthub.actionsjobsscheduler.service.ActionsJobSchedulerService;
import com.alerthub.actionsjobsscheduler.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {

		private final ActionService actionService;
	    private final ActionsJobSchedulerService schedulerService;
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
	    @PreAuthorize("hasRole('createAction')")
	    public ActionResponse create(@Valid @RequestBody ActionCreateRequest request) {
	        return actionService.createAction(request);
	    }

	    @GetMapping
	    @PreAuthorize("isAuthenticated()")
	    public List<ActionResponse> getAll() {
	        return actionService.getAllActions();
	    }

	    @GetMapping("/{id}")
	    @PreAuthorize("isAuthenticated()")
	    public ActionResponse getById(@PathVariable UUID id) {
	        return actionService.getById(id);
	    }

	    @PostMapping("/run-scheduler-now")
	    @ResponseStatus(HttpStatus.ACCEPTED)
	    @PreAuthorize("hasRole('triggerScan')")
	    public void runSchedulerNow() {
	        schedulerService.runSchedulerNow();
	    }
	    
	    @PutMapping("/{id}")
	    @PreAuthorize("hasRole('updateAction')")
	    public ActionResponse updateAction(
	            @PathVariable UUID id,
	            @RequestBody @Valid ActionUpdateRequest request) {
	        return actionService.updateAction(id, request);
	    }
	    
	    @DeleteMapping("/{id}")
	    @ResponseStatus(HttpStatus.NO_CONTENT)
	    @PreAuthorize("hasRole('deleteAction')")
	    public void deleteAction(@PathVariable UUID id) {
	        actionService.deleteAction(id);
	    }
}
