package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordPatchDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordResponseDTO;
import com.groovify.vinylshopapi.enums.Genre;
import com.groovify.vinylshopapi.enums.SortOrder;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.VinylRecordMapper;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import com.groovify.vinylshopapi.specifications.VinylRecordSpecification;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VinylRecordService {

    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordMapper vinylRecordMapper;

    public VinylRecordService(VinylRecordRepository vinylRecordRepository, VinylRecordMapper vinylRecordMapper) {
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordMapper = vinylRecordMapper;
    }

    public List<VinylRecordResponseDTO> getVinylRecords(String genre, BigDecimal minPrice, BigDecimal maxPrice,
                                                        Boolean isLimitedEdition, String orderBy, String sortOrder, Integer limit) {
        Sort sort = switch (orderBy.toLowerCase()) {
            case "price" -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("price") : Sort.Order.asc("price"));
            case "releasedate" -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("releaseDate") : Sort.Order.asc("releaseDate"));
            case "id" -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("id") : Sort.Order.asc("id"));
            default -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("title") : Sort.Order.asc("title"));
        };

        Specification<VinylRecord> specification = VinylRecordSpecification.withFilters(genre, minPrice, maxPrice, isLimitedEdition);
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
        VinylRecord vinylRecord = vinylRecordRepository.findByTitleContainingIgnoreCase(title)
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

        vinylRecord.setTitle(vinylRecordRequestDTO.getTitle());
        vinylRecord.setDescription(vinylRecordRequestDTO.getDescription());
        vinylRecord.setGenre(Genre.stringToGenre(vinylRecordRequestDTO.getGenre()));
        vinylRecord.setLabel(vinylRecordRequestDTO.getLabel());
        vinylRecord.setPrice(vinylRecordRequestDTO.getPrice());
        vinylRecord.setReleaseDate(vinylRecordRequestDTO.getReleaseDate());
        vinylRecord.setPlayTimeSeconds(vinylRecordRequestDTO.getPlayTimeSeconds());
        vinylRecord.setIsLimitedEdition(vinylRecordRequestDTO.getIsLimitedEdition());
        vinylRecord.setEan(vinylRecordRequestDTO.getEan());

        VinylRecord savedRecord = vinylRecordRepository.save(vinylRecord);
        return vinylRecordMapper.toResponseDTO(savedRecord);
    }

    public VinylRecordResponseDTO partialUpdateVinylRecord(Long id, VinylRecordPatchDTO vinylRecordPatchDTO) {
        VinylRecord vinylRecord = vinylRecordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + id + " not found"));

        if (vinylRecordPatchDTO.getTitle() != null && !vinylRecordPatchDTO.getTitle().trim().isEmpty()) {
            vinylRecord.setTitle(vinylRecordPatchDTO.getTitle());
        }

        if (vinylRecordPatchDTO.getDescription() != null) {
            vinylRecord.setDescription(vinylRecordPatchDTO.getDescription());
        }

        if (vinylRecordPatchDTO.getGenre() != null && !vinylRecordPatchDTO.getGenre().trim().isEmpty()) {
            vinylRecord.setGenre(Genre.stringToGenre(vinylRecordPatchDTO.getGenre()));
        }

        if (vinylRecordPatchDTO.getLabel() != null && !vinylRecordPatchDTO.getLabel().trim().isEmpty()) {
            vinylRecord.setLabel(vinylRecordPatchDTO.getLabel());
        }

        if (vinylRecordPatchDTO.getPrice() != null) {
            vinylRecord.setPrice(vinylRecordPatchDTO.getPrice());
        }

        if (vinylRecordPatchDTO.getReleaseDate() != null) {
            vinylRecord.setReleaseDate(vinylRecordPatchDTO.getReleaseDate());
        }

        if (vinylRecordPatchDTO.getPlayTimeSeconds() != null) {
            vinylRecord.setPlayTimeSeconds(vinylRecordPatchDTO.getPlayTimeSeconds());
        }

        if (vinylRecordPatchDTO.getIsLimitedEdition() != null) {
            vinylRecord.setIsLimitedEdition(vinylRecordPatchDTO.getIsLimitedEdition());
        }

        if (vinylRecordPatchDTO.getEan() != null) {
            vinylRecord.setEan(vinylRecordPatchDTO.getEan());
        }

        VinylRecord savedRecord = vinylRecordRepository.save(vinylRecord);
        return vinylRecordMapper.toResponseDTO(savedRecord);
    }

    public void deleteVinylRecord(Long id) {
        if (!vinylRecordRepository.existsById(id)) {
            throw new RecordNotFoundException("Vinyl record with id " + id + " not found");
        }
        vinylRecordRepository.deleteById(id);
    }
}
