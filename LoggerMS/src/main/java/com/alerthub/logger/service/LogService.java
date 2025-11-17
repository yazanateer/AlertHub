package com.alerthub.logger.service;

import java.time.Instant;
import java.util.List;

import com.alerthub.logger.dto.LogEntryRequest;
import com.alerthub.logger.model.LogEntry;
import com.alerthub.logger.model.LogLevel;

public interface LogService {

	LogEntry createLog(LogEntryRequest request);
	
	List<LogEntry> getAllLogs();
	
	List<LogEntry> getLogsByServiceName(String serviceName);
	
	List<LogEntry> getLogsByLogLevel(LogLevel logLevel);
	
	List<LogEntry> getLogsByServiceNameAndLogLevel(String serviceName, LogLevel logLevel);
	
	List<LogEntry> getLogsByTimestamp(Instant from, Instant to);
	
}
