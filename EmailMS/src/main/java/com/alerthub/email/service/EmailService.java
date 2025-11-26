package com.alerthub.email.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
