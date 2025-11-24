package com.alerthub.evaluation.service;

import com.alerthub.evaluation.dto.DeveloperLabelCountResponse;
import com.alerthub.evaluation.dto.DeveloperTaskCountResponse;
import com.alerthub.evaluation.dto.LabelAggregationResponse;

import java.util.List;

public interface EvaluationService {

    DeveloperLabelCountResponse findDeveloperWithMostLabel(String label, int sinceDays);

    List<LabelAggregationResponse> aggregateLabelsForDeveloper(Long developerId, int sinceDays);

    DeveloperTaskCountResponse countTasksForDeveloper(Long developerId, int sinceDays);
}
