package com.alerthub.sms.controllers;

import com.alerthub.sms.dto.SmsNotificationPayload;
import com.alerthub.sms.service.SmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmsNotificationConsumer {

    private final SmsService smsService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "#{@environment.getProperty('app.kafka.topic.sms')}",
            groupId = "#{@environment.getProperty('spring.kafka.consumer.group-id')}",
            containerFactory = "smsKafkaListenerContainerFactory"
    )
    public void consumeSmsMessage(String message) {
        try {
            SmsNotificationPayload payload =
                    objectMapper.readValue(message, SmsNotificationPayload.class);

            if (!"sms".equalsIgnoreCase(payload.type())) {
                log.warn("Received non-sms payload on sms topic: {}", payload.type());
                return;
            }

            smsService.sendSms(payload.to(), payload.message());

        } catch (Exception e) {
            log.error("Failed to process SMS notification message: {}", message, e);
        }
    }
}
