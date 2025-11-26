package com.alerthub.loader.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alerthub.loader.dto.ScanResult;
import com.alerthub.loader.model.PlatformInformation;
import com.alerthub.loader.service.LoaderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/loader")
@RequiredArgsConstructor
public class LoaderController {

	private final LoaderService loaderService;
	
	
	@PostMapping("scan")
	public ScanResult scan() {
		return loaderService.scan();
	}
	
	@GetMapping("/hello")
    public String sayHello() {
        return "Hello from LoaderMS";
    }
	
	@GetMapping("/platform-information")
    public List<PlatformInformation> getAll() {
        return loaderService.getAll();
    }
}
