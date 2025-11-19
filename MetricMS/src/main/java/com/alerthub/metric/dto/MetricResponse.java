package com.alerthub.metric.dto;

import java.util.UUID;

import com.alerthub.metric.model.Label;

public record MetricResponse(
			UUID id,
	        Integer userId,
	        String name,
	        Label label,
	        Integer threshold,
	        Integer timeFrameHours
	        ) {

}