package com.alerthub.evaluation.service;

public interface SmsNotificationProducerService {

    /**
     * Send an evaluation-related SMS notification to a phone number.
     *
     * @param toPhone phone number in local format, e.g. "0501234567"
     * @param message SMS text to send
     */
    void sendEvaluationSms(String toPhone, String message);
}
