package com.alerthub.evaluation.clients;

import com.alerthub.evaluation.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client to communicate with LoaderMS.
 *
 * LoaderMS is responsible for pulling data from Jira/GitHub/ClickUp
 * and storing it in the platformInformation database.
 */
@FeignClient(
        name = "loader-service",
        url = "${loader.service.base-url}",
        configuration = OpenFeignConfig.class
)
public interface LoaderClient {

    /**
     * Ask LoaderMS to sync platform information for the last N days
     * from all configured providers.
     *
     * LoaderMS side should implement:
     * GET /loader/sync?since={days}
     */
    @GetMapping("/loader/sync")
    void syncPlatformInformation(@RequestParam("since") int sinceDays);
}
