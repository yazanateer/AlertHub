package com.alerthub.loader.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alerthub.loader.model.ProviderType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileDiscoveryServiceImpl implements FileDiscoveryService{

	@Value("${loader.data-root-path}")
	private String dataRootPath;
	
	@Override
	public Optional<Path> findLatestFileForProvider(ProviderType provider) {
		
		String providerFolderName = provider.name().toLowerCase();
		Path providerDir = Paths.get(dataRootPath, providerFolderName);

		if(!Files.exists(providerDir) || !Files.isDirectory(providerDir)) {
			log.warn("Provider dir doesn't exist or isnt a dir: {}", providerDir.toAbsolutePath());
			return Optional.empty();
		}
		
		try(Stream<Path> stream = Files.list(providerDir)) {
			return stream.filter(Files::isRegularFile).max(Comparator.comparing(path -> {
				try {
					FileTime fileTime = Files.getLastModifiedTime(path);
					return fileTime.toMillis();
				} catch (IOException e) {
					log.warn("Failed to read lastModifiedtime for file: {}", path, e);
					return 0L; //1970's
				}
			}));
		}catch (IOException e) {
			log.error("Error while listing files in dir: " + providerDir.toAbsolutePath(), e);
			return Optional.empty();
		}
		
		
	}

}
