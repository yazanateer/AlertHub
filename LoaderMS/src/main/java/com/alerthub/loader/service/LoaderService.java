package com.alerthub.loader.service;

import com.alerthub.loader.dto.ScanResult;
import com.alerthub.loader.model.PlatformInformation;

public interface LoaderService {

	public PlatformInformation createTestRecord();
	
	public ScanResult scan();
}
