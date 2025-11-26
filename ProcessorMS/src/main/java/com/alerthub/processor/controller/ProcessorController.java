package com.alerthub.processor.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alerthub.processor.client.LoaderClinet;
import com.alerthub.processor.client.MetricClient;
import com.alerthub.processor.client.dto.MetricResponse;
import com.alerthub.processor.client.dto.PlatformInformation;
import com.alerthub.processor.dto.ActionMessage;
import com.alerthub.processor.service.ProcessorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProcessorController{

	private final ProcessorService processorService;
	private final LoaderClinet loaderClient;
	private final MetricClient metricClient;
	
	@GetMapping("/processor/consumer/loader")
	public void testLoader() {
			ActionMessage action = new ActionMessage();
	        action.setId("12345"); // or UUID
	        action.setActionType("EMAIL");
	        action.setTo("test@example.com");
	        action.setMessage("hello");

	        processorService.handleAction(action);

	}
	
	  @GetMapping("/api/test/platform-info")
	    public List<PlatformInformation> testGetAll() {
		  	log.info("consumer worked success");
	        return loaderClient.getAllPlatformInformation();
	    }
	  
	  @GetMapping("/api/test/metrics")
	    public List<MetricResponse> testGetMetrics(@RequestParam int userId) {
		  	log.info("consumer metrics by userId worked success");
	        return metricClient.getMetricsByUser(userId);
	    }
}
