package com.alerthub.actionsjobsscheduler.dto;

import java.time.LocalTime;

import com.alerthub.actionsjobsscheduler.model.ActionType;
import com.alerthub.actionsjobsscheduler.model.RunOnDay;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActionCreateRequest {
	    @NotNull
	    private String userId;

	    @NotBlank
	    private String name;

	    @NotBlank
	    private String condition;

	    @NotBlank
	    private String to;

	    @NotNull
	    private ActionType actionType;
	    @NotNull
	    private LocalTime runOnTime;

	    @NotNull
	    private RunOnDay runOnDay;

	    @NotBlank
	    private String message;
}
