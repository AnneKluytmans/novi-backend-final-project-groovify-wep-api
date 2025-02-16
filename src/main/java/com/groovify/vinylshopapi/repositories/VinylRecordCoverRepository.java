package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.VinylRecordCover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VinylRecordCoverRepository extends JpaRepository<VinylRecordCover, Long> {
}
