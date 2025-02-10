package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.VinylRecordStockPatchDTO;
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
@RequestMapping("/api/vinyl-records/stock")
public class VinylRecordStockController {

    private final VinylRecordStockService vinylRecordStockService;

    public VinylRecordStockController(VinylRecordStockService vinylRecordStockService) {
        this.vinylRecordStockService = vinylRecordStockService;
    }

    @GetMapping("/{vinylRecordId}")
    public ResponseEntity<VinylRecordStockResponseDTO> getStockByVinylRecord(@PathVariable Long vinylRecordId) {
        VinylRecordStockResponseDTO stock = vinylRecordStockService.getStockByVinylRecord(vinylRecordId);
        return ResponseEntity.ok(stock);
    }

    @PostMapping
    public ResponseEntity<?> createStock(@Valid @RequestBody VinylRecordStockRequestDTO stockRequestDTO,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        VinylRecordStockResponseDTO newStock = vinylRecordStockService.createStock(stockRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newStock.getId())
                .toUri();
        return ResponseEntity.created(location).body(newStock);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateStock(@PathVariable Long id,
                                         @Valid @RequestBody VinylRecordStockPatchDTO stockPatchDTO,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        VinylRecordStockResponseDTO updatedStock = vinylRecordStockService.updateStock(id, stockPatchDTO);
        return ResponseEntity.ok(updatedStock);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        vinylRecordStockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}
