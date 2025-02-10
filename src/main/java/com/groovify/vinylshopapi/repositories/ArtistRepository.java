package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long>, JpaSpecificationExecutor<Artist> {
    Optional<Artist> findByNameContainingIgnoreCase(String name);

    Boolean existsByName(String name);
}
