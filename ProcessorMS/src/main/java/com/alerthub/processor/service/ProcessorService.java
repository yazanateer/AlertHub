package com.alerthub.processor.service;

import org.springframework.stereotype.Service;

import com.alerthub.processor.dto.ActionMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProcessorService {

	public void handleAction(ActionMessage action) {
		log.info("Hanldign action: id={} , type={}, to={}, msg={}",
				action.getId(),
				action.getActionType(),
				action.getTo(),
				action.getMessage());

	}
}
