package com.alerthub.actionsjobsscheduler.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alerthub.actionsjobsscheduler.model.RunOnDay;
import com.alerthub.actionsjobsscheduler.model.Action;
import com.alerthub.actionsjobsscheduler.repository.ActionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionsJobSchedulerService {

	private final ActionRepository actionRepository;
	
	
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
            log.info("Action is due -> id={} name={} to={} type={}",
                    action.getId(), action.getName(), action.getTo(), action.getActionType());

            action.setLastRun(now);
        }
    }
}
