package com.alerthub.processor.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alerthub.processor.client.dto.PlatformInformation;

@FeignClient(
		name = "loader-ms",
		url = "${services.loader-ms.url}"
		)
public interface LoaderClinet {

	 @GetMapping("/loader/hello")
	    String getHello();
	 
	 @GetMapping("/loader/platform-information")
	 	List<PlatformInformation> getAllPlatformInformation();
}
