package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordPatchDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordResponseDTO;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.VinylRecordMapper;
import com.groovify.vinylshopapi.models.Artist;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.ArtistRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import com.groovify.vinylshopapi.specifications.VinylRecordSpecification;

import com.groovify.vinylshopapi.utils.SortHelper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VinylRecordService {

    private final ArtistRepository artistRepository;
    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordMapper vinylRecordMapper;

    public VinylRecordService(ArtistRepository artistRepository, VinylRecordRepository vinylRecordRepository, VinylRecordMapper vinylRecordMapper) {
        this.artistRepository = artistRepository;
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordMapper = vinylRecordMapper;
    }

    public List<VinylRecordResponseDTO> getVinylRecords(
            String title,
            String genre,
            String artist,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isLimitedEdition,
            Boolean isAvailable,
            String sortBy,
            String sortOrder,
            Integer limit
    ) {
        Sort sort;
        if (sortBy.trim().equalsIgnoreCase("bestselling")) {
            Sort.Direction direction = Sort.Direction.DESC;
            sort = Sort.by(direction, "stock.amountSold");
        } else {
            sort = SortHelper.getSort(sortBy, sortOrder, List.of("id", "price", "releaseDate", "title"));
        }
        Specification<VinylRecord> specification = VinylRecordSpecification.filterVinylRecords(
                title, genre, artist, minPrice, maxPrice, isLimitedEdition, isAvailable
        );
        List<VinylRecord> vinylRecords = vinylRecordRepository.findAll(specification, sort);

        if (limit != null && limit > 0 && limit < vinylRecords.size()) {
            vinylRecords = vinylRecords.subList(0, limit);
        }

        return vinylRecordMapper.toResponseDTOs(vinylRecords);
    }

    public VinylRecordResponseDTO getVinylRecordById(Long id) {
        VinylRecord vinylRecord = vinylRecordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + id + " not found"));

        return vinylRecordMapper.toResponseDTO(vinylRecord);
    }

    public VinylRecordResponseDTO getVinylRecordByTitle(String title) {
        VinylRecord vinylRecord = vinylRecordRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with title " + title + " not found"));

        return vinylRecordMapper.toResponseDTO(vinylRecord);
    }

    public VinylRecordResponseDTO createVinylRecord(VinylRecordRequestDTO vinylRecordRequestDTO) {
        VinylRecord vinylRecord = vinylRecordMapper.toEntity(vinylRecordRequestDTO);

        VinylRecord savedRecord = vinylRecordRepository.save(vinylRecord);
        return vinylRecordMapper.toResponseDTO(savedRecord);
    }

    public VinylRecordResponseDTO updateVinylRecord(Long id, VinylRecordRequestDTO vinylRecordRequestDTO) {
        VinylRecord vinylRecord = vinylRecordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + id + " not found"));

        vinylRecordMapper.updateVinylRecord(vinylRecordRequestDTO, vinylRecord);

        VinylRecord savedRecord = vinylRecordRepository.save(vinylRecord);
        return vinylRecordMapper.toResponseDTO(savedRecord);
    }

    public VinylRecordResponseDTO partialUpdateVinylRecord(Long id, VinylRecordPatchDTO vinylRecordPatchDTO) {
        VinylRecord vinylRecord = vinylRecordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + id + " not found"));

       vinylRecordMapper.partialUpdateVinylRecord(vinylRecordPatchDTO, vinylRecord);

        VinylRecord savedRecord = vinylRecordRepository.save(vinylRecord);
        return vinylRecordMapper.toResponseDTO(savedRecord);
    }

    public void deleteVinylRecord(Long id) {
        if (!vinylRecordRepository.existsById(id)) {
            throw new RecordNotFoundException("Vinyl record with id " + id + " not found");
        }
        vinylRecordRepository.deleteById(id);
    }


    public void addArtistToVinyl(Long vinylRecordId, Long artistId) {
        VinylRecord vinylRecord = vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + vinylRecordId + " not found"));

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RecordNotFoundException("Artist with id " + artistId + " not found"));

        vinylRecord.setArtist(artist);
        vinylRecordRepository.save(vinylRecord);
    }

    public void removeArtistFromVinyl(Long vinylRecordId) {
        VinylRecord vinylRecord = vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + vinylRecordId + " not found"));

        vinylRecord.setArtist(null);
        vinylRecordRepository.save(vinylRecord);
    }
}
