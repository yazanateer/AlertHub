package com.alerthub.actionsjobsscheduler.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alerthub.actionsjobsscheduler.model.RunOnDay;
import com.alerthub.actionsjobsscheduler.kafka.JobEvent;
import com.alerthub.actionsjobsscheduler.kafka.KafkaTopics;
import com.alerthub.actionsjobsscheduler.model.Action;
import com.alerthub.actionsjobsscheduler.repository.ActionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionsJobSchedulerService {

	private final ActionRepository actionRepository;
	private final KafkaTemplate<String, JobEvent> kafkaTemplate;
	
	
    @Scheduled(cron = "0 0/30 * * * *")
    public void scheduleActions() {
        runSchedulerLogic();
    }

    public void runSchedulerNow() {
        runSchedulerLogic();
    }
    
    
	 @Transactional
	 public void runSchedulerLogic() {
        LocalDateTime now = LocalDateTime.now();
        RunOnDay today = RunOnDay.valueOf(now.getDayOfWeek().name());
        LocalTime timeSlot = now.toLocalTime().withSecond(0).withNano(0);

        log.info("Scheduler running at {} | day={} | timeSlot={}", now, today, timeSlot);

        List<RunOnDay> days = List.of(today, RunOnDay.ALL);

        List<Action> dueActions = actionRepository
                .findByIsEnabledTrueAndIsDeletedFalseAndRunOnTimeAndRunOnDayIn(timeSlot, days);

        if (dueActions.isEmpty()) {
            log.info("No actions to schedule at this time.");
            return;
        }

        for (Action action : dueActions) {
            JobEvent event = JobEvent.builder()
            		.actionId(action.getId())
	        		.userId(action.getUserId())
	                .to(action.getTo())
	                .message(action.getMessage())
	                .actionType(action.getActionType())
	                .condition(action.getCondition())
	                .runOnDay(action.getRunOnDay())
	                .runOnTime(action.getRunOnTime())
	                .build();
            //send to kafka (producer)
            kafkaTemplate.send(
            		KafkaTopics.ACTION_JOBS_TOPIC,
            		action.getId().toString(),
            		event
            		);
            
            log.info("Sent JobEvent to Kafka | topic={} | key={} | to={}",
                    KafkaTopics.ACTION_JOBS_TOPIC,
                    action.getId(),
                    action.getTo());
            
            action.setLastRun(now);
	    }
    }
}
