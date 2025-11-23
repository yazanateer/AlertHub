package com.alerthub.evaluation.controllers;

import com.alerthub.evaluation.dto.DeveloperLabelCountResponse;
import com.alerthub.evaluation.dto.DeveloperTaskCountResponse;
import com.alerthub.evaluation.dto.LabelAggregationResponse;
import com.alerthub.evaluation.service.EvaluationService;
import com.alerthub.evaluation.service.NotificationProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final NotificationProducerService notificationProducerService;

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal != null ? principal.toString() : "unknown";
    }

    // 1. Developer with most occurrences of label
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/developer/mostlabel")
    public DeveloperLabelCountResponse getDeveloperWithMostLabel(
            @RequestParam("label") String label,
            @RequestParam("since") int sinceDays
    ) {
        DeveloperLabelCountResponse result =
                evaluationService.findDeveloperWithMostLabel(label, sinceDays);

        String managerUser = getCurrentUsername();
        String subject = "Evaluation: Top developer for label '%s'".formatted(label);
        String body = "Manager '%s' requested evaluation.\nDeveloper %d has %d tasks with label '%s' in the last %d days."
                .formatted(managerUser, result.developerId(), result.count(), result.label(), sinceDays);

        // Manager email can be looked up later; for now just send username as key.
        notificationProducerService.sendEvaluationEmail(managerUser, subject, body);

        return result;
    }

    // 2. Aggregation of each label for a developer
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/developer/{developerId}/labelaggregate")
    public List<LabelAggregationResponse> getLabelAggregationForDeveloper(
            @PathVariable("developerId") Long developerId,
            @RequestParam("since") int sinceDays
    ) {
        List<LabelAggregationResponse> result =
                evaluationService.aggregateLabelsForDeveloper(developerId, sinceDays);

        String managerUser = getCurrentUsername();
        String subject = "Evaluation: Label distribution for developer %d".formatted(developerId);

        StringBuilder body = new StringBuilder(
                "Manager '%s' requested label aggregation for developer %d in last %d days:\n"
                        .formatted(managerUser, developerId, sinceDays));
        result.forEach(r ->
                body.append("- ").append(r.label()).append(": ").append(r.count()).append("\n"));

        notificationProducerService.sendEvaluationEmail(managerUser, subject, body.toString());

        return result;
    }

    // 3. Total number of tasks for a developer
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/developer/{developerId}/task-amount")
    public DeveloperTaskCountResponse getTaskAmountForDeveloper(
            @PathVariable("developerId") Long developerId,
            @RequestParam("since") int sinceDays
    ) {
        DeveloperTaskCountResponse result =
                evaluationService.countTasksForDeveloper(developerId, sinceDays);

        String managerUser = getCurrentUsername();
        String subject = "Evaluation: Task count for developer %d".formatted(developerId);
        String body = "Manager '%s' requested task count.\nDeveloper %d has %d tasks in the last %d days."
                .formatted(managerUser, result.developerId(), result.totalTasks(), sinceDays);

        notificationProducerService.sendEvaluationEmail(managerUser, subject, body);

        return result;
    }
}
