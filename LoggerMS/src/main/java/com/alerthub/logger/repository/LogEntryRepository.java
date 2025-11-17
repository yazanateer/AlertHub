package com.alerthub.logger.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.alerthub.logger.model.LogEntry;
import com.alerthub.logger.model.LogLevel;

@Repository
public interface LogEntryRepository extends MongoRepository<LogEntry, String>{
	
	List<LogEntry> findByServiceName(String serviceName);
	
	List<LogEntry> findByLogLevel(LogLevel logLevel);
	
	List<LogEntry> findByServiceNameAndLogLevel(String serviceName, LogLevel logLevel);
	
	List<LogEntry> findByTimestampBetween(Instant from, Instant to);
	
}
