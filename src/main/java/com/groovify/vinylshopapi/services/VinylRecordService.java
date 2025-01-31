package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordResponseDTO;
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
}
