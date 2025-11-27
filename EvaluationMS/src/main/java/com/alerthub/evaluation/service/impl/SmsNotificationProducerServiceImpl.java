package com.alerthub.evaluation.service.impl;

import com.alerthub.evaluation.service.SmsNotificationProducerService;
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
public class SmsNotificationProducerServiceImpl implements SmsNotificationProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.sms}")
    private String smsTopic;

    @Override
    public void sendEvaluationSms(String toPhone, String message) {
        SmsNotificationPayload payload = SmsNotificationPayload.builder()
                .type("sms")
                .to(toPhone)
                .message(message)
                .build();

        try {
            String json = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(smsTopic, toPhone, json);
            log.info("Sent SMS evaluation notification to topic={} for phone={}", smsTopic, toPhone);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize SMS payload for phone={}", toPhone, e);
        }
    }

    @Builder
    private record SmsNotificationPayload(
            String type,   // must be "sms" (SmsMS will check this)
            String to,     // phone number
            String message
    ) {}
}
