package com.alerthub.evaluation.controllers;

import com.alerthub.evaluation.dto.DeveloperLabelCountResponse;
import com.alerthub.evaluation.dto.DeveloperTaskCountResponse;
import com.alerthub.evaluation.dto.LabelAggregationResponse;
import com.alerthub.evaluation.service.EvaluationService;
import com.alerthub.evaluation.service.JwtService;
import com.alerthub.evaluation.service.NotificationProducerService;
import com.alerthub.evaluation.service.SmsNotificationProducerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final NotificationProducerService notificationProducerService;      // email
    private final SmsNotificationProducerService smsNotificationProducerService; // sms
    private final JwtService jwtService;

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        return authHeader.substring(7);
    }

    private String getManagerEmailFromJwt(String token) {
        String email = jwtService.extractEmail(token);
        // fallback to username if email is not present
        return (email != null && !email.isBlank())
                ? email
                : jwtService.extractUsername(token);
    }

    private String getManagerPhoneFromJwt(String token) {
        // assumes JWT includes a "phone" claim
        try {
            String phone = jwtService.extractPhone(token);
            return (phone != null && !phone.isBlank()) ? phone : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 1. Developer with most occurrences of a specific label within a time frame.
     *
     * GET /evaluation/developer/mostlabel?label={labelName}&since={day}
     */
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/developer/mostlabel")
    public DeveloperLabelCountResponse getDeveloperWithMostLabel(
            @RequestParam("label") String label,
            @RequestParam("since") int sinceDays,
            HttpServletRequest request
    ) {
        String token = extractToken(request);
        String managerEmail = getManagerEmailFromJwt(token);
        String managerUsername = jwtService.extractUsername(token);
        String managerPhone = getManagerPhoneFromJwt(token);

        DeveloperLabelCountResponse result =
                evaluationService.findDeveloperWithMostLabel(label, sinceDays);

        String subject = "Evaluation: Top developer for label '%s'".formatted(label);
        String body = "Manager '%s' requested evaluation.\nDeveloper %d has %d tasks with label '%s' in the last %d days."
                .formatted(managerUsername, result.developerId(), result.count(), result.label(), sinceDays);

        // EMAIL via Kafka -> EmailMS -> Gmail
        notificationProducerService.sendEvaluationEmail(managerEmail, subject, body);

        // Optional SMS via Kafka -> SmsMS -> iRestSMS
        if (managerPhone != null) {
            smsNotificationProducerService.sendEvaluationSms(managerPhone, body);
        }

        return result;
    }

    /**
     * 2. Aggregation of each label for the specified developer within a time frame.
     *
     * GET /evaluation/developer/{developer_id}/labelaggregate?since={day}
     */
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/developer/{developerId}/labelaggregate")
    public List<LabelAggregationResponse> getLabelAggregationForDeveloper(
            @PathVariable("developerId") Long developerId,
            @RequestParam("since") int sinceDays,
            HttpServletRequest request
    ) {
        String token = extractToken(request);
        String managerEmail = getManagerEmailFromJwt(token);
        String managerUsername = jwtService.extractUsername(token);
        String managerPhone = getManagerPhoneFromJwt(token);

        List<LabelAggregationResponse> result =
                evaluationService.aggregateLabelsForDeveloper(developerId, sinceDays);

        String subject = "Evaluation: Label distribution for developer %d".formatted(developerId);

        StringBuilder body = new StringBuilder(
                "Manager '%s' requested label aggregation for developer %d in last %d days:\n"
                        .formatted(managerUsername, developerId, sinceDays));

        result.forEach(r ->
                body.append("- ").append(r.label()).append(": ").append(r.count()).append("\n"));

        // EMAIL
        notificationProducerService.sendEvaluationEmail(managerEmail, subject, body.toString());

        // Optional SMS
        if (managerPhone != null) {
            smsNotificationProducerService.sendEvaluationSms(managerPhone, body.toString());
        }

        return result;
    }

    /**
     * 3. Total number of tasks assigned to a specified developer within a time frame.
     *
     * GET /evaluation/developer/{developer_id}/task-amount?since={day}
     */
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/developer/{developerId}/task-amount")
    public DeveloperTaskCountResponse getTaskAmountForDeveloper(
            @PathVariable("developerId") Long developerId,
            @RequestParam("since") int sinceDays,
            HttpServletRequest request
    ) {
        String token = extractToken(request);
        String managerEmail = getManagerEmailFromJwt(token);
        String managerUsername = jwtService.extractUsername(token);
        String managerPhone = getManagerPhoneFromJwt(token);

        DeveloperTaskCountResponse result =
                evaluationService.countTasksForDeveloper(developerId, sinceDays);

        String subject = "Evaluation: Task count for developer %d".formatted(developerId);
        String body = "Manager '%s' requested task count.\nDeveloper %d has %d tasks in the last %d days."
                .formatted(managerUsername, result.developerId(), result.totalTasks(), sinceDays);

        // EMAIL
        notificationProducerService.sendEvaluationEmail(managerEmail, subject, body);

        // Optional SMS
        if (managerPhone != null) {
            smsNotificationProducerService.sendEvaluationSms(managerPhone, body);
        }

        return result;
    }
}
