package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordResponseDTO;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.VinylRecordMapper;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class VinylRecordService {

    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordMapper vinylRecordMapper;

    public VinylRecordService(VinylRecordRepository vinylRecordRepository, VinylRecordMapper vinylRecordMapper) {
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordMapper = vinylRecordMapper;
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
