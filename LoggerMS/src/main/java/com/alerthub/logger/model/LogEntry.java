package com.alerthub.logger.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection="logs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEntry {

	@Id
	private String id;
	
	private Instant timestamp;
	
	@Setter
	private String serviceName;
	
	@Setter
	private LogLevel logLevel;
	
	@Setter
	private String message;

}
