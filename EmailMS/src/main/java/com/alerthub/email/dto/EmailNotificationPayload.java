package com.alerthub.email.dto;

import lombok.Builder;

@Builder
public record EmailNotificationPayload(
        String type,    // "email"
        String to,      // email address
        String subject,
        String body
) {}
