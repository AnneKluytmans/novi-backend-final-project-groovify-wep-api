package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.VinylRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VinylRecordRepository extends JpaRepository<VinylRecord, Long> {
    Optional<VinylRecord> findByTitleContainingIgnoreCase(String title);
}
