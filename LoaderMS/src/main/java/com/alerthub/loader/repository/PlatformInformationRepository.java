package com.alerthub.loader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alerthub.loader.model.PlatformInformation;

@Repository
public interface PlatformInformationRepository extends JpaRepository<PlatformInformation, Long>{

}
