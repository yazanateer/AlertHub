package com.alerthub.actionsjobsscheduler.dto;

import java.time.LocalTime;

import com.alerthub.actionsjobsscheduler.model.ActionType;
import com.alerthub.actionsjobsscheduler.model.RunOnDay;

import lombok.Data;

@Data
public class ActionUpdateRequest {

		private String name;
	    private String condition;
	    private String to;
	    private ActionType actionType;
	    private LocalTime runOnTime;
	    private RunOnDay runOnDay;
	    private String message;
	    private Boolean enabled;
}
