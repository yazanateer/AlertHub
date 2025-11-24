package com.alerthub.actionsjobsscheduler.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.alerthub.actionsjobsscheduler.model.ActionType;
import com.alerthub.actionsjobsscheduler.model.RunOnDay;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionResponse {
	    private UUID id;
	    private String userId;
	    private String name;
	    private String condition;
	    private String to;
	    private ActionType actionType;
	    private LocalTime runOnTime;
	    private RunOnDay runOnDay;
	    private String message;
	    private boolean enabled;
	    private boolean deleted;
	    private LocalDateTime createDate;
	    private LocalDateTime lastUpdate;
	    private LocalDateTime lastRun;
}
