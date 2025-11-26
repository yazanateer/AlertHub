package com.alerthub.processor.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alerthub.processor.client.LoaderClinet;
import com.alerthub.processor.client.dto.MetricResponse;
import com.alerthub.processor.dto.ActionMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessorService {

	private final LoaderClinet loaderClient;
	
	public void handleAction(ActionMessage action) {
//		log.info("Hanldign action: id={} , type={}, to={}, msg={}",
//				action.getId(),
//				action.getActionType(),
//				action.getTo(),
//				action.getMessage());
		
		log.info("Handling action id={}", action.getId());
        String response = loaderClient.getHello();
        log.info("Received from LoaderMS: {}", response);
	}
	
	
}
