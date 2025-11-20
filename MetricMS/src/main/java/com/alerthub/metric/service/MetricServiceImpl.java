package com.alerthub.metric.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alerthub.metric.dto.MetricCreateRequest;
import com.alerthub.metric.dto.MetricResponse;
import com.alerthub.metric.dto.MetricUpdateRequest;
import com.alerthub.metric.exceptions.MetricAlreadyExistsException;
import com.alerthub.metric.exceptions.MetricNotFoundException;
import com.alerthub.metric.model.Metric;
import com.alerthub.metric.repository.MetricRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MetricServiceImpl implements MetricService {

	private final MetricRepository metricRepository;

	@Override
	public MetricResponse createMetric(MetricCreateRequest request) {

		log.info("Creating metric for userId={}, name={}", request.userId(), request.name());
		if (metricRepository.existsByUserIdAndName(request.userId(), request.name())) {
			log.warn("Metric already exists for userId={}, name={}", request.userId(), request.name());
			throw new MetricAlreadyExistsException(request.userId());
		}

		Metric metricEntity = Metric.builder()
				.userId(request.userId())
				.name(request.name())
				.label(request.label())
				.threshold(request.threshold()) 
				.timeFrameHours(request.timeFrameHours())
				.build();

		Metric saved = metricRepository.save(metricEntity);
		log.debug("Metric created with id={}", saved.getId());
		return toResponse(saved);

	}

	@Override
	@Transactional(readOnly = true)
	public List<MetricResponse> getMetricByUser(int userId) {
		log.info("fetching metrics for userID: {}", userId);

		return metricRepository.findByUserId(userId).stream().map(this::toResponse).toList();

	}

	@Override
	@Transactional(readOnly = true)
	public MetricResponse getMetricById(Long id, Integer userId) {
		log.info("fetching metrics id {} for userId: {}", id, userId);

		Metric entity = metricRepository.findByIdAndUserId(id, userId).orElseThrow(() -> {
			log.warn("Metric not found id={} for userId={}", id, userId);
			return new MetricNotFoundException(id);
		});

		return toResponse(entity);
	}

	@Override
	public MetricResponse updateMetric(Long id, Integer userId, MetricUpdateRequest request) {
		log.info("Updating metric id={} for userId={}", id, userId);

		Metric entity = metricRepository.findByIdAndUserId(id, userId).orElseThrow(() -> {
			log.warn("Metric not found id={} for userId={}", id, userId);
			return new MetricNotFoundException(id);
		});

		// Check unique name per user
		if (!entity.getName().equals(request.name())
				&& metricRepository.existsByUserIdAndName(userId, request.name())) {
			log.warn("Metric name already used for userId={}, name={}", userId, request.name());
			throw new MetricAlreadyExistsException(request.name());
		}

		entity.setName(request.name());
		entity.setLabel(request.label());
		entity.setThreshold(request.threshold());
		entity.setTimeFrameHours(request.timeFrameHours());

		Metric saved = metricRepository.save(entity);

		log.debug("Metric updated id={}", saved.getId());
		return toResponse(saved);
	}

	@Override
	public void deleteMetric(Long id, Integer userId) {
		log.info("Deleting metric id={} for userId={}", id, userId);

		Metric entity = metricRepository.findByIdAndUserId(id, userId).orElseThrow(() -> {
			log.warn("Metric not found id={} for userId={}", id, userId);
			return new MetricNotFoundException(id);
		});

		metricRepository.delete(entity);
		log.debug("Metric deleted id={}", id);
	}

	private MetricResponse toResponse(Metric e) {
		return new MetricResponse(e.getId(), e.getUserId(), e.getName(), e.getLabel(), e.getThreshold(),
				e.getTimeFrameHours());
	}

}
