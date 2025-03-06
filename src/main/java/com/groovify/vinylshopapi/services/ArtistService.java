package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.ArtistPatchDTO;
import com.groovify.vinylshopapi.dtos.ArtistRequestDTO;
import com.groovify.vinylshopapi.dtos.ArtistResponseDTO;
import com.groovify.vinylshopapi.exceptions.DeleteOperationException;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.ArtistMapper;
import com.groovify.vinylshopapi.models.Artist;
import com.groovify.vinylshopapi.repositories.ArtistRepository;

import com.groovify.vinylshopapi.specifications.ArtistSpecification;
import com.groovify.vinylshopapi.utils.SortHelper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    public ArtistService(ArtistRepository artistRepository, ArtistMapper artistMapper) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
    }

    public List<ArtistResponseDTO> getArtists(
            String country, String name, Integer minPopularity, Integer maxPopularity,
            String sortBy, String sortOrder, Integer limit
    ) {
        Sort sort = SortHelper.getSort(sortBy, sortOrder, List.of("id", "popularity", "name", "countryOfOrigin"));
        Specification<Artist> specification = ArtistSpecification.filterArtists(
                country, name, minPopularity, maxPopularity
        );
        List<Artist> artists = artistRepository.findAll(specification, sort);

        if (limit != null && limit > 0 && limit < artists.size()) {
            artists = artists.subList(0, limit);
        }

        return artistMapper.toResponseDTOs(artists);
    }

    public ArtistResponseDTO getArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Artist with id " + id + " not found"));

        return artistMapper.toResponseDTO(artist);
    }

    public ArtistResponseDTO getArtistByName(String name) {
        Artist artist = artistRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RecordNotFoundException("Artist with name " + name + " not found"));

        return artistMapper.toResponseDTO(artist);
    }

    public ArtistResponseDTO createArtist(ArtistRequestDTO artistRequestDTO) {
        Artist artist = artistMapper.toEntity(artistRequestDTO);

        if (artistRepository.existsByName(artist.getName())) {
            throw new ConflictException("Artist with name " + artist.getName() + " already exists");
        }

        Artist savedArtist = artistRepository.save(artist);
        return artistMapper.toResponseDTO(savedArtist);
    }

    public ArtistResponseDTO updateArtist(Long id, ArtistRequestDTO artistRequestDTO) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Artist with id " + id + " not found"));

        validateUniqueArtistName(artistRequestDTO.getName(), id);

        artist.setName(artistRequestDTO.getName());
        artist.setIsGroup(artistRequestDTO.getIsGroup());
        artist.setDebutDate(artistRequestDTO.getDebutDate());
        artist.setCountryOfOrigin(artistRequestDTO.getCountryOfOrigin());
        artist.setPopularity(artistRequestDTO.getPopularity());

        Artist savedArtist = artistRepository.save(artist);
        return artistMapper.toResponseDTO(savedArtist);
    }

    public ArtistResponseDTO partialUpdateArtist(Long id, ArtistPatchDTO artistPatchDTO) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Artist with id " + id + " not found"));


        if (artistPatchDTO.getName() != null && !artistPatchDTO.getName().trim().isEmpty()) {
            validateUniqueArtistName(artistPatchDTO.getName(), id);
            artist.setName(artistPatchDTO.getName());
        }

        if (artistPatchDTO.getIsGroup() != null) {
            artist.setIsGroup(artistPatchDTO.getIsGroup());
        }

        if (artistPatchDTO.getDebutDate() != null) {
            artist.setDebutDate(artistPatchDTO.getDebutDate());
        }

        if (artistPatchDTO.getCountryOfOrigin() != null && !artistPatchDTO.getCountryOfOrigin().trim().isEmpty()) {
            artist.setCountryOfOrigin(artistPatchDTO.getCountryOfOrigin());
        }

        if (artistPatchDTO.getPopularity() != null) {
            artist.setPopularity(artistPatchDTO.getPopularity());
        }

        Artist savedArtist = artistRepository.save(artist);
        return artistMapper.toResponseDTO(savedArtist);
    }

    public void deleteArtist(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Artist with id " + id + " not found"));

        if (!artist.getVinylRecords().isEmpty()) {
            throw new DeleteOperationException("Artist " + artist.getName() + " cannot be deleted while linked to vinyl records");
        }

        artistRepository.deleteById(id);
    }

    private void validateUniqueArtistName(String name, Long currentArtistId) {
        Optional<Artist> existingArtist = artistRepository.findByNameIgnoreCase(name);

        if (existingArtist.isPresent() && !existingArtist.get().getId().equals(currentArtistId)) {
            throw new ConflictException("Artist with name " + name + " already exists");
        }
    }
}

