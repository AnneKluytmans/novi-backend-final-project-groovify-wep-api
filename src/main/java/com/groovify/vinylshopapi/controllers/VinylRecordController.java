package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.VinylRecordResponseDTO;
import com.groovify.vinylshopapi.services.VinylRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vinyl-records")
public class VinylRecordController {

    private final VinylRecordService vinylRecordService;

    public VinylRecordController(VinylRecordService vinylRecordService) {
        this.vinylRecordService = vinylRecordService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VinylRecordResponseDTO> getVinylRecordById(@PathVariable Long id) {
        VinylRecordResponseDTO vinylRecord = vinylRecordService.getVinylRecordById(id);
        return ResponseEntity.ok(vinylRecord);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<VinylRecordResponseDTO> getVinylRecordByTitle(@PathVariable String title) {
        VinylRecordResponseDTO vinylRecord = vinylRecordService.getVinylRecordByTitle(title);
        return ResponseEntity.ok(vinylRecord);
    }
}
