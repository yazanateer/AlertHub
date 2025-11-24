package com.alerthub.actionsjobsscheduler.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alerthub.actionsjobsscheduler.dto.ActionCreateRequest;
import com.alerthub.actionsjobsscheduler.dto.ActionResponse;
import com.alerthub.actionsjobsscheduler.service.ActionService;
import com.alerthub.actionsjobsscheduler.service.ActionsJobSchedulerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {

	 private final ActionService actionService;
	    private final ActionsJobSchedulerService schedulerService;

	    @PostMapping
	    @ResponseStatus(HttpStatus.CREATED)
	    public ActionResponse create(@Valid @RequestBody ActionCreateRequest request) {
	        return actionService.createAction(request);
	    }

	    @GetMapping
	    public List<ActionResponse> getAll() {
	        return actionService.getAllActions();
	    }

	    @GetMapping("/{id}")
	    public ActionResponse getById(@PathVariable UUID id) {
	        return actionService.getById(id);
	    }

	    @PostMapping("/run-scheduler-now")
	    @ResponseStatus(HttpStatus.ACCEPTED)
	    public void runSchedulerNow() {
	        schedulerService.runSchedulerNow();
	    }
}
