package com.alerthub.loader.service;

import java.nio.file.Path;
import java.util.Optional;

import com.alerthub.loader.model.ProviderType;

public interface FileDiscoveryService {

	Optional<Path> findLatestFileForProvider(ProviderType provider);
}
