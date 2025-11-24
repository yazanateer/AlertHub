package com.alerthub.actionsjobsscheduler.kafka;

import java.time.LocalTime;
import java.util.UUID;

import com.alerthub.actionsjobsscheduler.model.ActionType;
import com.alerthub.actionsjobsscheduler.model.RunOnDay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobEvent {

	private UUID actionId;
	private String userId;
	private String to;
	private String message;
	private ActionType actionType;
	private String condition;
	private RunOnDay runOnDay;
	private LocalTime runOnTime;
}
