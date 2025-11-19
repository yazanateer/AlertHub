package com.alerthub.loader.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alerthub.loader.model.ProcessedFile;
import com.alerthub.loader.model.ProviderType;

@Repository
public interface ProcessedFileRepository extends JpaRepository<ProcessedFile, Long>{

    boolean existsByProviderAndFileName(ProviderType provider, String fileName);

}
