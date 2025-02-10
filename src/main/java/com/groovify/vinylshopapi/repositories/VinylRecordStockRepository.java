package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.models.VinylRecordStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VinylRecordStockRepository extends JpaRepository<VinylRecordStock, Long> {
    Optional<VinylRecordStock> findByVinylRecord(VinylRecord vinylRecord);
}
