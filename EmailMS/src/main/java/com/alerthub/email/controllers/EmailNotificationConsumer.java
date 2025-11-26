package com.alerthub.email.controllers;

import com.alerthub.email.dto.EmailNotificationPayload;
import com.alerthub.email.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.email}")
    private String emailTopic;

    @KafkaListener(
            topics = "#{@environment.getProperty('app.kafka.topic.email')}",
            groupId = "#{@environment.getProperty('spring.kafka.consumer.group-id')}",
            containerFactory = "emailKafkaListenerContainerFactory"
    )
    public void consumeEmailMessage(String message) {
        try {
            EmailNotificationPayload payload =
                    objectMapper.readValue(message, EmailNotificationPayload.class);

            if (!"email".equalsIgnoreCase(payload.type())) {
                log.warn("Received non-email payload on email topic: {}", payload.type());
                return;
            }

            emailService.sendEmail(payload.to(), payload.subject(), payload.body());

        } catch (Exception e) {
            log.error("Failed to process email notification message: {}", message, e);
        }
    }
}
