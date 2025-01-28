package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Artist;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByCountryOfOriginContainingIgnoreCase(String countryOfOrigin, Sort sort);
    List<Artist> findByCountryOfOriginContainingIgnoreCaseAndPopularityBetween(String countryOfOrigin, Integer minPopularity, Integer maxPopularity, Sort sort);
    List<Artist> findByCountryOfOriginContainingIgnoreCaseAndPopularityGreaterThanEqual(String countryOfOrigin, Integer minPopularity, Sort sort);
    List<Artist> findByCountryOfOriginContainingIgnoreCaseAndPopularityLessThanEqual(String countryOfOrigin, Integer maxPopularity, Sort sort);

    List<Artist> findByPopularityBetween(Integer minPopularity, Integer maxPopularity, Sort sort);
    List<Artist> findByPopularityGreaterThanEqual(Integer minPopularity, Sort sort);
    List<Artist> findByPopularityLessThanEqual(Integer maxPopularity, Sort sort);

    Optional<Artist> findByNameContainingIgnoreCase(String name);
}
