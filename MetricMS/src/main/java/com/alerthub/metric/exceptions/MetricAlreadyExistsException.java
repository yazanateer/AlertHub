package com.alerthub.metric.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class MetricAlreadyExistsException extends ResponseStatusException {

	public MetricAlreadyExistsException(int id) {
        super(HttpStatus.NOT_FOUND, "Metric not found with id: " + id);
    }
}