package com.alerthub.sms.dto;

import lombok.Builder;

@Builder
public record SmsNotificationPayload(
        String type,   // "sms"
        String to,     // phone number, e.g. "0501234567"
        String message
) {}
