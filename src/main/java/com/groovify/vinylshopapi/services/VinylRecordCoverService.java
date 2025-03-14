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
    private final ValidationUtils validationUtils;

    public VinylRecordCoverService(
            VinylRecordRepository vinylRecordRepository,
            VinylRecordCoverRepository vinylRecordCoverRepository,
            VinylRecordCoverMapper vinylRecordCoverMapper,
            ValidationUtils validationUtils
    ) {
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordCoverRepository = vinylRecordCoverRepository;
        this.vinylRecordCoverMapper = vinylRecordCoverMapper;
        this.validationUtils = validationUtils;
    }

    public VinylRecordCoverResponseDTO uploadCover(Long vinylRecordId, MultipartFile file, String downloadUrl) throws IOException {
        validationUtils.validateFile(file, allowedFileTypes);

        VinylRecord vinylRecord = findVinylRecord(vinylRecordId);

        if (vinylRecord.getCover() != null) {
            throw new ConflictException("Vinyl record with id " + vinylRecordId + " already has a cover");
        }

        VinylRecordCover cover = new VinylRecordCover();
        cover.setFilename(file.getOriginalFilename());
        cover.setFileType(file.getContentType());
        cover.setDownloadUrl(downloadUrl);
        cover.setData(file.getBytes());

        VinylRecordCover savedCover = vinylRecordCoverRepository.save(cover);

        vinylRecord.setCover(savedCover);
        vinylRecordRepository.save(vinylRecord);

        return vinylRecordCoverMapper.toResponseDTO(savedCover);
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
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + vinylRecordId + " not found"));
    }

    private VinylRecordCover findVinylRecordCover(VinylRecord vinylRecord) {
        return Optional.ofNullable(vinylRecord.getCover())
                .orElseThrow(() -> new RecordNotFoundException("Cover of vinyl record " + vinylRecord.getTitle() + " not found"));
    }
}

