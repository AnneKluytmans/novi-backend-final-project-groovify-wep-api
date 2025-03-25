package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordCoverDownloadDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordCoverResponseDTO;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.VinylRecordCoverMapper;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.models.VinylRecordCover;
import com.groovify.vinylshopapi.repositories.VinylRecordCoverRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;

import com.groovify.vinylshopapi.validation.ValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class VinylRecordCoverService {

    private final List<String> allowedFileTypes = List.of("image/jpeg", "image/png");

    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordCoverRepository vinylRecordCoverRepository;
    private final VinylRecordCoverMapper vinylRecordCoverMapper;

    public VinylRecordCoverService(
            VinylRecordRepository vinylRecordRepository,
            VinylRecordCoverRepository vinylRecordCoverRepository,
            VinylRecordCoverMapper vinylRecordCoverMapper
    ) {
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordCoverRepository = vinylRecordCoverRepository;
        this.vinylRecordCoverMapper = vinylRecordCoverMapper;
    }

    public VinylRecordCoverResponseDTO uploadCover(Long vinylRecordId, MultipartFile file, String downloadUrl) throws IOException {
        ValidationUtils.validateFile(file, allowedFileTypes);

        VinylRecord vinylRecord = findVinylRecord(vinylRecordId);

        if (vinylRecord.getCover() != null) {
            throw new ConflictException("Cover already exists for vinyl record with id: '" + vinylRecordId);
        }

        VinylRecordCover cover = new VinylRecordCover(file, downloadUrl);

        vinylRecord.setCover(cover);
        vinylRecordRepository.save(vinylRecord);

        return vinylRecordCoverMapper.toResponseDTO(cover);
    }

    public VinylRecordCoverDownloadDTO downloadCover(Long vinylRecordId) {
        VinylRecord vinylRecord = findVinylRecord(vinylRecordId);
        VinylRecordCover cover = findVinylRecordCover(vinylRecord);

        return vinylRecordCoverMapper.toDownloadDTO(cover);
    }

    public void deleteCover(Long vinylRecordId) {
        VinylRecord vinylRecord = findVinylRecord(vinylRecordId);
        VinylRecordCover cover = findVinylRecordCover(vinylRecord);

        vinylRecord.setCover(null);

        vinylRecordCoverRepository.delete(cover);
    }


    private VinylRecord findVinylRecord(Long vinylRecordId) {
        return vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("No vinyl record found with id: " + vinylRecordId));
    }

    private VinylRecordCover findVinylRecordCover(VinylRecord vinylRecord) {
        return Optional.ofNullable(vinylRecord.getCover())
                .orElseThrow(() -> new RecordNotFoundException("No cover found for vinyl record: " + vinylRecord.getTitle()));
    }
}

