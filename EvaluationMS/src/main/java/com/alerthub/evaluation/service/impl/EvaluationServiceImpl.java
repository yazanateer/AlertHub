package com.alerthub.evaluation.service.impl;

import com.alerthub.evaluation.dto.DeveloperLabelCountResponse;
import com.alerthub.evaluation.dto.DeveloperTaskCountResponse;
import com.alerthub.evaluation.dto.LabelAggregationResponse;
import com.alerthub.evaluation.exceptions.ResourceNotFoundException;
import com.alerthub.evaluation.repository.PlatformInformationRepository;
import com.alerthub.evaluation.repository.projection.DeveloperLabelCountProjection;
import com.alerthub.evaluation.repository.projection.LabelCountProjection;
import com.alerthub.evaluation.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final PlatformInformationRepository platformRepo;

    @Override
    public DeveloperLabelCountResponse findDeveloperWithMostLabel(String label, int sinceDays) {
        LocalDateTime since = LocalDateTime.now().minusDays(sinceDays);

        DeveloperLabelCountProjection projection =
                platformRepo.findTopDeveloperByLabelSince(label, since);

        if (projection == null) {
            throw new ResourceNotFoundException("No developer found for label '%s'".formatted(label));
        }

        return DeveloperLabelCountResponse.builder()
                .developerId(projection.getDeveloperId())
                .label(projection.getLabel())
                .count(projection.getCount())
                .build();
    }

    @Override
    public List<LabelAggregationResponse> aggregateLabelsForDeveloper(Long developerId, int sinceDays) {
        LocalDateTime since = LocalDateTime.now().minusDays(sinceDays);
        List<LabelCountProjection> list =
                platformRepo.aggregateLabelsForDeveloperSince(developerId, since);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No label data found for developer id " + developerId);
        }

        return list.stream()
                .map(p -> LabelAggregationResponse.builder()
                        .label(p.getLabel())
                        .count(p.getCount())
                        .build())
                .toList();
    }

    @Override
    public DeveloperTaskCountResponse countTasksForDeveloper(Long developerId, int sinceDays) {
        LocalDateTime since = LocalDateTime.now().minusDays(sinceDays);
        long count = platformRepo.countTasksForDeveloperSince(developerId, since);

        if (count == 0L) {
            throw new ResourceNotFoundException(
                    "No tasks found for developer id " + developerId);
        }

        return DeveloperTaskCountResponse.builder()
                .developerId(developerId)
                .totalTasks(count)
                .build();
    }
}
