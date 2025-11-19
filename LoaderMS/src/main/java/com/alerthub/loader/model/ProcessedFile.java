package com.alerthub.loader.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "processed_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedFile {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderType provider;      // GITHUB / CLICKUP / JIRA

    @Column(name = "file_name", nullable = false)
    private String fileName;            // e.g. github_2025_11_03T19_30_00.csv

    @Column(name = "file_path", nullable = false)
    private String filePath;            // full path on disk

    @Column(name = "file_last_modified", nullable = false)
    private LocalDateTime fileLastModified; // last modified timestamp

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;  // when Loader processed this file

    @Column(name = "records_inserted", nullable = false)
    private Integer recordsInserted;
    
}
