package com.alerthub.loader.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.alerthub.loader.dto.ScanResult;
import com.alerthub.loader.model.PlatformInformation;
import com.alerthub.loader.repository.PlatformInformationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoaderServiceImpl implements LoaderService{

	private final PlatformInformationRepository platformInformationRepository;
	
	@Override
	public PlatformInformation createTestRecord() {
	
		PlatformInformation entity = new PlatformInformation();
		
	 	entity.setTimestamp(LocalDateTime.now());     // today as scan date
        entity.setOwnerId("manager-123");
        entity.setProject("Test Project");
        entity.setTag("backend");
        entity.setLabel("LOW");
        entity.setDeveloperId("dev-001");
        entity.setTaskNumber("TASK-001");
        entity.setEnvironment("DEV");
        entity.setUserStory("Test user story from LoaderServiceImpl");
        entity.setTaskPoint(5);
        entity.setSprint("Sprint 1");
        
        return platformInformationRepository.save(entity);
	}
	
	
	
	
	 	@Override
	    public ScanResult scan() {
	        // 1. Find latest files for each provider
	        // 2. Parse content
	        // 3. Map to PlatformInformation
	        // 4. Save to DB and count inserted rows

	        // For now, just return a placeholder result so the API works
	        LocalDateTime now = LocalDateTime.now();
	        long insertedRecords = 0L;
	        String message = "Scan placeholder executed - logic not implemented yet";

	        return new ScanResult(now, insertedRecords, message);
	    }
	 
	 

}
