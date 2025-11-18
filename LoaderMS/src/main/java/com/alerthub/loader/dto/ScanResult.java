package com.alerthub.loader.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScanResult {

	
	private LocalDateTime scanTime;

    private long insertedRecords;

    private String message;
    
}
