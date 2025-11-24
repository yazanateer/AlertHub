package com.alerthub.evaluation.dto;

import lombok.Builder;

@Builder
public record LabelAggregationResponse(
        String label,
        Long count
) {}
