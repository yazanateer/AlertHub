package com.alerthub.evaluation.service;

public interface NotificationProducerService {
    void sendEvaluationEmail(String toEmail, String subject, String body);
}
