package com.alerthub.sms.service.impl;

import com.alerthub.sms.client.SMSApiClient;
import com.alerthub.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsServiceImpl implements SmsService {

    private final SMSApiClient smsApiClient;

    @Override
    public void sendSms(String to, String message) {
        try {
            String response = smsApiClient.sendSMS(to, message);
            log.info("SMS sent to {}. API response: {}", to, response);
        } catch (Exception e) {
            log.error("Failed to send SMS to {}", to, e);
        }
    }
}
