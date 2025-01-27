package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.ArtistResponseDTO;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.ArtistMapper;
import com.groovify.vinylshopapi.models.Artist;
import com.groovify.vinylshopapi.repositories.ArtistRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    public ArtistService(ArtistRepository artistRepository, ArtistMapper artistMapper) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
    }

    public ArtistResponseDTO getArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Artist with id " + id + " not found"));
        ArtistResponseDTO artistResponseDTO = artistMapper.toResponseDTO(artist);
        artistResponseDTO.setYearsSinceDebut(calculateYearsSinceDebut(artist.getDebutDate()));
        return artistResponseDTO;
    }

    private int calculateYearsSinceDebut(LocalDate debutDate) {
        if (debutDate == null) {
            return 0;
        }
        return Period.between(debutDate, LocalDate.now()).getYears();
    }
}

