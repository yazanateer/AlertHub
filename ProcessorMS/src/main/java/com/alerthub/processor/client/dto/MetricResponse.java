package com.alerthub.processor.client.dto;

public record MetricResponse(
        Long id,
        Integer userId,
        String name,
        String label,
        Integer threshold,
        Integer timeFrameHours
) {}
