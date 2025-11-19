package com.alerthub.loader.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.alerthub.loader.dto.ScanResult;
import com.alerthub.loader.model.PlatformInformation;
import com.alerthub.loader.model.ProcessedFile;
import com.alerthub.loader.model.ProviderType;
import com.alerthub.loader.repository.PlatformInformationRepository;
import com.alerthub.loader.repository.ProcessedFileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoaderServiceImpl implements LoaderService{

	private final PlatformInformationRepository platformInformationRepository;
	private final FileDiscoveryService fileDiscoveryService;
	private final ProcessedFileRepository processedFileRepository;

	
	 	@Override
	    public ScanResult scan() {
	 		LocalDateTime now = LocalDateTime.now();
	 		
	 		long totalInserted = 0L;
	 		StringBuilder messageBuilder = new StringBuilder("Scan summary: ");
	 		
	 		for(ProviderType provider : ProviderType.values()) {
	 			long insertedForProvider = 0L;
	 			Optional<Path> latestFileOpt = fileDiscoveryService.findLatestFileForProvider(provider);
	 			
	 			if(latestFileOpt.isEmpty()) {
	 				log.info("No files found for provider {}", provider);
	 				messageBuilder.append(provider.name())
	 				.append(" no files found");
	 				continue;
	 			}
	 			
	 			Path latestFile = latestFileOpt.get();
	 	        String fileName = latestFile.getFileName().toString();
	 	        String filePath = latestFile.toAbsolutePath().toString();
	 	        
	 	        LocalDateTime fileLastModified = getFileLastModified(latestFile);
	 	       log.info("Latest {} file found: {} (lastModified={})",provider.name(), filePath, fileLastModified);
	 		
	 			
	 	      boolean alreadyProcessed = processedFileRepository.existsByProviderAndFileName(provider, fileName);

	 	    	if (alreadyProcessed) {
	 	    	    log.info("File {} for provider {} was already processed, skipping.", fileName, provider);
	 	    	    messageBuilder
	 	    	            .append(provider.name())
	 	    	            .append(" => no new file to process (")
	 	    	            .append(fileName)
	 	    	            .append("); ");
	 	    	    continue;
	 	    	}
	 			insertedForProvider = loadFile(latestFile);
	 			totalInserted += insertedForProvider;
	 		
	 			ProcessedFile processed = ProcessedFile.builder()
	 					.provider(provider)
	 					.fileName(fileName)
	 					.filePath(filePath)
	 					.fileLastModified(fileLastModified)
	 					.processedAt(LocalDateTime.now())
	 					.recordsInserted((int) insertedForProvider)
	 					.build();
	 			
	 	        processedFileRepository.save(processed);
	 	        
	 	        
	 			messageBuilder.append(provider.name())
                .append(" => file ")
                .append(latestFile.getFileName())
                .append(", inserted records: ")
                .append(insertedForProvider)
                .append("; ");
	 		}
	 		
	 		return new ScanResult(now, totalInserted, messageBuilder.toString());
	 		
	    }

		private long loadFile(Path dataFile) {
		 
			List<PlatformInformation> batch = new ArrayList<>();
			
			try(Stream<String> lines = Files.lines(dataFile, StandardCharsets.UTF_8)) {
				Iterator<String> iterator = lines.iterator();
				
				if(!iterator.hasNext()) {
					log.warn("{} file {} is empty",dataFile.getFileName());
					return 0;
				}
				while(iterator.hasNext()) {
					String line = iterator.next().trim();
					if(line.isEmpty()) {
						continue;
					}
					String[] cols = line.split(",", -1);
					if(cols.length < 10) {
						log.warn("Skipping line in {} becuase not enough column: {}", dataFile.getFileName(), line);
						continue;
					}
					PlatformInformation entity = new PlatformInformation();
					entity.setTimestamp(LocalDateTime.now());
					entity.setOwnerId(cols[1].trim());            
		            entity.setProject(cols[2].trim()); 
		            entity.setTag(cols[3].trim()); 
		            entity.setLabel(cols[4].trim());  
		            entity.setDeveloperId(cols[5].trim()); 
		            entity.setTaskNumber(cols[6].trim()); 
		            entity.setEnvironment(cols[7].trim()); 
		            entity.setUserStory(cols[8].trim());           
		            entity.setTaskPoint(parseIntOrZero(cols[9]));
		            entity.setSprint(cols[10].trim());
		            batch.add(entity);

				}
			} catch(IOException e) {
				log.error("Error while reading file: {}", dataFile.toAbsolutePath(), e);
				return 0L;
			}
			
			  if (batch.isEmpty()) {
		        log.info("No valid records parsed from file {}", dataFile.getFileName());
		        return 0L;
		    }
			  
			 platformInformationRepository.saveAll(batch);
		     log.info("Saved {} records from GitHub file {}", batch.size(), dataFile.getFileName());

		     return batch.size();
		}


		
		private LocalDateTime getFileLastModified(Path file) {
		    try {
		        return LocalDateTime.ofInstant(
		                Files.getLastModifiedTime(file).toInstant(),
		                java.time.ZoneId.systemDefault()
		        );
		    } catch (IOException e) {
		        log.warn("Failed to read lastModifiedTime for file {}, using now() as fallback",
		                file.toAbsolutePath(), e);
		        return LocalDateTime.now();
		    }
		}



		private int parseIntOrZero(String val) {
		if (val == null) {
			return 0;
		}
		String trimmed = val.trim();
		if (trimmed.isEmpty()) {
			return 0;
		}
		try {
			return Integer.parseInt(trimmed);
		}catch (NumberFormatException e) {
		   log.warn("Failed to parse integer from '{}', defaulting to 0", val);
	       return 0;
	       }
		}
	 
	 

}
