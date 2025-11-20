package com.alerthub.metric.exceptions;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MetricNotFoundException extends ResponseStatusException {

    public MetricNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Metric not found with id: " + id);
    }
}