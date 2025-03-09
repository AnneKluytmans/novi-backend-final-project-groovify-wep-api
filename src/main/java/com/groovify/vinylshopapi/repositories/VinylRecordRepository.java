package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.VinylRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VinylRecordRepository extends JpaRepository<VinylRecord, Long>, JpaSpecificationExecutor<VinylRecord> {
    Optional<VinylRecord> findByTitleIgnoreCase(String title);
    Boolean existsByTitleIgnoreCaseAndReleaseDate(String title, LocalDate releaseDate);
}
