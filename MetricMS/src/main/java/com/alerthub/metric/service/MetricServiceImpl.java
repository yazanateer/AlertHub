package com.alerthub.metric.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.alerthub.metric.dto.MetricCreateRequest;
import com.alerthub.metric.dto.MetricResponse;
import com.alerthub.metric.dto.MetricUpdateRequest;
import com.alerthub.metric.exceptions.MetricAlreadyExistsException;
import com.alerthub.metric.model.Metric;
import com.alerthub.metric.repository.MetricRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MetricServiceImpl implements MetricService{

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
				 .label(request.label())
				 .threshold(request.threshold())
				 .timeFrameHours(request.timeFrameHours())
				 .build();
		 
		 Metric saved = metricRepository.save(metricEntity);	
         log.debug("Metric created with id={}", saved.getId());
		 return toResponse(saved);
		 
	}

	@Override
	public List<MetricResponse> getMetricByUser(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetricResponse getMetricById(UUID id, Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetricResponse updateMetric(UUID id, Integer userId, MetricUpdateRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMetric(UUID id, Integer userId) {
		// TODO Auto-generated method stub
		
	}
	
	private MetricResponse toResponse(Metric e) {
        return new MetricResponse(
                e.getId(),
                e.getUserId(),
                e.getName(),
                e.getLabel(),
                e.getThreshold(),
                e.getTimeFrameHours()
        );
    }

}
