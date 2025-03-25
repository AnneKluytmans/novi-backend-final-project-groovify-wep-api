package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.VinylRecordStockRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordStockResponseDTO;
import com.groovify.vinylshopapi.services.VinylRecordStockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/vinyl-records/{id}/stock")
public class VinylRecordStockController {

    private final VinylRecordStockService vinylRecordStockService;

    public VinylRecordStockController(VinylRecordStockService vinylRecordStockService) {
        this.vinylRecordStockService = vinylRecordStockService;
    }

    @GetMapping()
    public ResponseEntity<VinylRecordStockResponseDTO> getStock(
            @PathVariable("id") Long vinylRecordId
    ) {
        VinylRecordStockResponseDTO stock = vinylRecordStockService.getStock(vinylRecordId);
        return ResponseEntity.ok(stock);
    }

    @PostMapping
    public ResponseEntity<?> createStock(
            @PathVariable("id") Long vinylRecordId,
            @Valid @RequestBody VinylRecordStockRequestDTO stockRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        VinylRecordStockResponseDTO newStock = vinylRecordStockService.createStock(vinylRecordId, stockRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity.created(location).body(newStock);
    }

    @PutMapping()
    public ResponseEntity<?> updateStock(
            @PathVariable("id") Long vinylRecordId,
            @Valid @RequestBody VinylRecordStockRequestDTO stockRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        VinylRecordStockResponseDTO updatedStock = vinylRecordStockService.updateStock(vinylRecordId, stockRequestDTO);
        return ResponseEntity.ok(updatedStock);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteStock(
            @PathVariable("id") Long vinylRecordId
    ) {
        vinylRecordStockService.deleteStock(vinylRecordId);
        return ResponseEntity.noContent().build();
    }
}
