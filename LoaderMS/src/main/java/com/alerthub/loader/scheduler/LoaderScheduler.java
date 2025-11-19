package com.alerthub.loader.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alerthub.loader.dto.ScanResult;
import com.alerthub.loader.service.LoaderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoaderScheduler {

	private final LoaderService loaderService;
	
	@Scheduled(cron = "0 0 * * * *")
	public void hourlyScan() {
		log.info("Scheduled scan started...");
		try {
			ScanResult result = loaderService.scan();
			log.info("Scheduled scan finished: {}", result.getMessage());
		} catch(Exception e){
            log.error("Scheduled scan failed", e);
		}
	}
}
