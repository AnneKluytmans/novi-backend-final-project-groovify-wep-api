package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.ArtistResponseDTO;
import com.groovify.vinylshopapi.enums.SortOrder;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.ArtistMapper;
import com.groovify.vinylshopapi.models.Artist;
import com.groovify.vinylshopapi.repositories.ArtistRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    public ArtistService(ArtistRepository artistRepository, ArtistMapper artistMapper) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
    }

    public List<ArtistResponseDTO> getArtists(String country, Integer minPopularity, Integer maxPopularity,
                                              String orderBy, String sortOrder, Integer limit) {
        Sort sort;
        if ("popularity".equalsIgnoreCase(orderBy)) {
            sort = SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ?
                    Sort.by("popularity").descending() : Sort.by("popularity").ascending();
        } else if ("id".equalsIgnoreCase(orderBy)) {
            sort = SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ?
                    Sort.by("id").descending() : Sort.by("id").ascending();
        } else {
            sort = SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ?
                    Sort.by("name").descending() : Sort.by("name").ascending();
        }

        List<Artist> artists;

        if (country != null && !country.trim().isEmpty()) {
            if (minPopularity != null && maxPopularity != null) {
                artists = artistRepository.findByCountryOfOriginContainingIgnoreCaseAndPopularityBetween(country, minPopularity, maxPopularity, sort);
            } else if (minPopularity != null) {
                artists = artistRepository.findByCountryOfOriginContainingIgnoreCaseAndPopularityGreaterThanEqual(country, minPopularity, sort);
            } else if (maxPopularity != null) {
                artists = artistRepository.findByCountryOfOriginContainingIgnoreCaseAndPopularityLessThanEqual(country, maxPopularity, sort);
            } else {
                artists = artistRepository.findByCountryOfOriginContainingIgnoreCase(country, sort);
            }
        } else if (minPopularity != null && maxPopularity != null) {
            artists = artistRepository.findByPopularityBetween(minPopularity, maxPopularity, sort);
        } else if (minPopularity != null) {
            artists = artistRepository.findByPopularityGreaterThanEqual(minPopularity, sort);
        } else if (maxPopularity != null) {
            artists = artistRepository.findByPopularityLessThanEqual(maxPopularity, sort);
        } else {
            artists = artistRepository.findAll(sort);
        }

        if (limit != null && limit > 0) {
            artists = artists.subList(0, Math.min(limit, artists.size()));
        }

        return artistMapper.toResponseDTOs(artists);
    }

    public ArtistResponseDTO getArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Artist with id " + id + " not found"));

        return artistMapper.toResponseDTO(artist);
    }

}

