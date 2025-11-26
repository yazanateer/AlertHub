package com.alerthub.processor.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alerthub.processor.client.dto.MetricResponse;


@FeignClient(
        name = "metric-ms",
        url = "${services.metric-ms.url}"
)
public interface MetricClient {

    @GetMapping("/api/metrics")
    List<MetricResponse> getMetricsByUser(@RequestParam("userId") int userId);
}