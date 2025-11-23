package com.alerthub.actionsjobsscheduler.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="action")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Action {

	@Id
	@GeneratedValue(strategy= GenerationType.UUID)
	private UUID id;
	
	 @Column(name = "user_id", nullable = false)
	private String userId;
	
	 @Column(name = "name", nullable = false)
	private String name;
	
    @Column(name = "create_date", nullable = false)
	private LocalDateTime createDate;
	
    @Column(name = "condition_text", columnDefinition = "TEXT", nullable = false)
    private String condition;
    
    @Column(name = "to_val", nullable = false)
	private String to;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
	private ActionType actionType;
	
    @Column(name = "run_on_time", nullable = false)
	private LocalTime runOnTime;
	
    @Enumerated(EnumType.STRING)
    @Column(name = "run_on_day", nullable = false)
	private RunOnDay runOnDay;
	
    @Column(name = "message", nullable = false)
	private String message;
	
    @Column(name = "is_enabled", nullable = false)
	private boolean isEnabled;
	
    @Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;
	
    @Column(name = "last_update")
	private LocalDateTime lastUpdate;
	
    @Column(name = "last_run")
	private LocalDateTime lastRun;
}
