package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.VinylRecordCoverDownloadDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordCoverResponseDTO;
import com.groovify.vinylshopapi.services.VinylRecordCoverService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/vinyl-records/{id}/cover")
public class VinylRecordCoverController {

    private final VinylRecordCoverService vinylRecordCoverService;

    public VinylRecordCoverController(VinylRecordCoverService vinylRecordCoverService) {
        this.vinylRecordCoverService = vinylRecordCoverService;
    }

    @PostMapping("/upload")
    public ResponseEntity<VinylRecordCoverResponseDTO> uploadCover(@PathVariable("id") Long vinylRecordId,
                                                                   @RequestParam("file") MultipartFile file) throws IOException {
        VinylRecordCoverResponseDTO newCover = vinylRecordCoverService.uploadCover(vinylRecordId, file);

        URI location = URI.create(newCover.getDownloadUrl());

        return ResponseEntity.created(location).body(newCover);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadCover(@PathVariable("id") Long vinylRecordId) {
        VinylRecordCoverDownloadDTO cover = vinylRecordCoverService.downloadCover(vinylRecordId);

        MediaType mediaType;

        try {
            mediaType = MediaType.parseMediaType(cover.getFileType());
        } catch (InvalidMediaTypeException e) {
             mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + cover.getFilename() + "\"")
                .body(cover.getData());
    }

}

