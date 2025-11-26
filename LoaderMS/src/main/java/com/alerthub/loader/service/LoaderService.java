package com.alerthub.loader.service;

import java.util.List;

import com.alerthub.loader.dto.ScanResult;
import com.alerthub.loader.model.PlatformInformation;

public interface LoaderService {

	public ScanResult scan();
	
    public List<PlatformInformation> getAll();

}
