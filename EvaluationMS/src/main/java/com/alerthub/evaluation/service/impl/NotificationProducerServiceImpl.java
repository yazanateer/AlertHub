package com.alerthub.evaluation.service.impl;

import com.alerthub.evaluation.service.NotificationProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducerServiceImpl implements NotificationProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.email}")
    private String emailTopic;

    @Override
    public void sendEvaluationEmail(String toEmail, String subject, String body) {
        EmailNotificationPayload payload = EmailNotificationPayload.builder()
                .type("email")
                .to(toEmail)
                .subject(subject)
                .body(body)
                .build();

        try {
            String json = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(emailTopic, toEmail, json);
            log.info("Sent evaluation result to Kafka topic={} for email={}", emailTopic, toEmail);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize email payload", e);
        }
    }

    @Builder
    private record EmailNotificationPayload(
            String type,
            String to,
            String subject,
            String body
    ) {}
}
