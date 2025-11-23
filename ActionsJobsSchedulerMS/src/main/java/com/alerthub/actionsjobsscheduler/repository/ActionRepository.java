package com.alerthub.actionsjobsscheduler.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.alerthub.actionsjobsscheduler.model.Action;
import com.alerthub.actionsjobsscheduler.model.RunOnDay;

@Repository
public interface ActionRepository extends JpaRepository<Action, UUID>{
	 
	List<Action> findByIsEnabledTrueAndIsDeletedFalseAndRunOnTimeAndRunOnDayIn(
	            LocalTime runOnTime,
	            List<RunOnDay> days
	    );
}
