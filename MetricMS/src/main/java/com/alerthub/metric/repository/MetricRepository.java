package com.alerthub.metric.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alerthub.metric.model.Metric;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long>{

	List<Metric> findByUserId(int userId);
	
	Optional<Metric> findByIdAndUserId(Long id, Integer userId);
	
    boolean existsByUserIdAndName(Integer userId, String name);

}
