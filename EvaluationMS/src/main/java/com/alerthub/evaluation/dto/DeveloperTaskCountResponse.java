package com.alerthub.evaluation.dto;

import lombok.Builder;

@Builder
public record DeveloperTaskCountResponse(
        Long developerId,
        Long totalTasks
) {}
