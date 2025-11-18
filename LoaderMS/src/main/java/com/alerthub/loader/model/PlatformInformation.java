package com.alerthub.loader.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="platformInformation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="timestamp")
	private LocalDateTime timestamp;
	
	@Column(name="owner_id")
	private String ownerId;
	
	@Column(name="project")
	private String project;
	
	@Column(name="tag")
	private String tag;
	
	@Column(name="label")
	private String label;
	
	@Column(name="developer_id")
	private String developerId;
	
	@Column(name="task_number")
	private String taskNumber;
	
	@Column(name="environment")
	private String environment;
	
	@Column(name="user_story")
	private String userStory;
	
	@Column(name="task_point")
	private int taskPoint;
	
	@Column(name="sprint")
	private String sprint;
}
