package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.VinylRecordStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VinylRecordStockRepository extends JpaRepository<VinylRecordStock, Long> {
}
