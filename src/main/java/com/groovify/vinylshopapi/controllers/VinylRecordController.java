package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.VinylRecordResponseDTO;
import com.groovify.vinylshopapi.services.VinylRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/vinyl-records")
public class VinylRecordController {

    private final VinylRecordService vinylRecordService;

    public VinylRecordController(VinylRecordService vinylRecordService) {
        this.vinylRecordService = vinylRecordService;
    }

    @GetMapping
    public ResponseEntity<List<VinylRecordResponseDTO>> getVinylRecords(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean isLimitedEdition,
            @RequestParam(defaultValue = "title") String orderBy,
            @RequestParam(defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) Integer limit)
    {
        List<VinylRecordResponseDTO> vinylRecords = vinylRecordService.getVinylRecords(genre, minPrice, maxPrice, isLimitedEdition,
                orderBy, sortOrder, limit);
        return ResponseEntity.ok(vinylRecords);
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
