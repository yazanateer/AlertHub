package com.alerthub.evaluation.dto;

import lombok.Builder;

@Builder
public record DeveloperLabelCountResponse(
        Long developerId,
        String label,
        Long count
) {}
