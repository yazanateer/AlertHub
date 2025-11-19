package com.alerthub.metric.dto;

import com.alerthub.metric.model.Label;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MetricCreateRequest(
		@NotNull int userId,
		@NotBlank String name,
		@NotNull Label label,
		@NotNull @Min(1) int threshold,
		@NotNull @Min(1) @Max(24) int timeFrameHours
		) {

}
