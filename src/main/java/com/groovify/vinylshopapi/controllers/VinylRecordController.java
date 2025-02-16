package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.VinylRecordPatchDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordResponseDTO;
import com.groovify.vinylshopapi.services.VinylRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
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
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean isLimitedEdition,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(defaultValue = "title") String orderBy,
            @RequestParam(defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) Integer limit)
    {
        List<VinylRecordResponseDTO> vinylRecords = vinylRecordService.getVinylRecords(genre, artist, minPrice, maxPrice, isLimitedEdition,
                isAvailable, orderBy, sortOrder, limit);
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

    @PostMapping
    public ResponseEntity<?> createVinylRecord(@Valid @RequestBody VinylRecordRequestDTO vinylRecordRequestDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        VinylRecordResponseDTO newVinylRecord = vinylRecordService.createVinylRecord(vinylRecordRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newVinylRecord.getId())
                .toUri();
        return ResponseEntity.created(location).body(newVinylRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVinylRecord(@PathVariable Long id,
                                               @Valid @RequestBody VinylRecordRequestDTO vinylRecordRequestDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        VinylRecordResponseDTO updatedVinylRecord = vinylRecordService.updateVinylRecord(id, vinylRecordRequestDTO);
        return ResponseEntity.ok(updatedVinylRecord);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateVinylRecord(@PathVariable Long id,
                                                      @Valid @RequestBody VinylRecordPatchDTO vinylRecordPatchDTO,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        VinylRecordResponseDTO updatedVinylRecord = vinylRecordService.partialUpdateVinylRecord(id, vinylRecordPatchDTO);
        return ResponseEntity.ok(updatedVinylRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVinylRecord(@PathVariable Long id) {
        vinylRecordService.deleteVinylRecord(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{vinylId}/artist/{artistId}")
    public ResponseEntity<VinylRecordResponseDTO> linkArtistToVinyl(@PathVariable Long vinylId, @PathVariable Long artistId) {
        VinylRecordResponseDTO linkedVinylRecord = vinylRecordService.linkArtistToVinyl(vinylId, artistId);
        return ResponseEntity.ok(linkedVinylRecord);
    }

    @PutMapping("/{vinylId}/artist")
    public ResponseEntity<VinylRecordResponseDTO> unlinkArtistFromVinyl(@PathVariable Long vinylId) {
        VinylRecordResponseDTO unlinkedVinylRecord = vinylRecordService.unlinkArtistFromVinyl(vinylId);
        return ResponseEntity.ok(unlinkedVinylRecord);
    }

}
