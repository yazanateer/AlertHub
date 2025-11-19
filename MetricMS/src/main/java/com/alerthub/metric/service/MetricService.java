package com.alerthub.metric.service;

import java.util.List;
import java.util.UUID;

import com.alerthub.metric.dto.MetricCreateRequest;
import com.alerthub.metric.dto.MetricResponse;
import com.alerthub.metric.dto.MetricUpdateRequest;

public interface MetricService {

	MetricResponse createMetric(MetricCreateRequest request);
	
	List<MetricResponse> getMetricByUser(int userId);
	
	MetricResponse getMetricById(UUID id, Integer userId);

    MetricResponse updateMetric(UUID id, Integer userId, MetricUpdateRequest request);

    void deleteMetric(UUID id, Integer userId);
}
