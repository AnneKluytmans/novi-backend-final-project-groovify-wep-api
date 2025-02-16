package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordCoverDownloadDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordCoverResponseDTO;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.InvalidFileTypeException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.VinylRecordCoverMapper;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.models.VinylRecordCover;
import com.groovify.vinylshopapi.repositories.VinylRecordCoverRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class VinylRecordCoverService {

    private final List<String> allowedFileTypes = List.of("image/jpeg", "image/png");

    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordCoverRepository vinylRecordCoverRepository;
    private final VinylRecordCoverMapper vinylRecordCoverMapper;

    public VinylRecordCoverService(VinylRecordRepository vinylRecordRepository, VinylRecordCoverRepository vinylRecordCoverRepository, VinylRecordCoverMapper vinylRecordCoverMapper) {
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordCoverRepository = vinylRecordCoverRepository;
        this.vinylRecordCoverMapper = vinylRecordCoverMapper;
    }

    public VinylRecordCoverResponseDTO uploadCover(Long vinylRecordId, MultipartFile file, String downloadUrl) throws IOException {
        if (file.isEmpty()) {
            throw new InvalidFileTypeException("Uploaded file is empty");
        }

        if (!allowedFileTypes.contains(file.getContentType())) {
            throw new InvalidFileTypeException("File type is invalid. Only JPEG and PNG files are allowed.");
        }

        VinylRecord vinylRecord = vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + vinylRecordId + " not found"));


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
        VinylRecord vinylRecord = vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + vinylRecordId + " not found"));

        VinylRecordCover cover = vinylRecord.getCover();

        if (cover == null) {
            throw new RecordNotFoundException("Cover of vinyl record with id " + vinylRecordId + " not found");
        }

        return vinylRecordCoverMapper.toDownloadDTO(cover);
    }

}

