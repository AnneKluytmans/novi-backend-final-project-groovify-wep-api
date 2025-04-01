package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.VinylRecordSummaryResponseDTO;
import com.groovify.vinylshopapi.services.CustomerFavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/customers/{id}/favorite-records")
public class CustomerFavoritesController {

    private final CustomerFavoritesService customerFavoritesService;

    public CustomerFavoritesController(
            CustomerFavoritesService customerFavoritesService
    ) {
        this.customerFavoritesService = customerFavoritesService;
    }

    @GetMapping()
    public ResponseEntity<List<VinylRecordSummaryResponseDTO>> getCustomerFavoriteRecords(
            @PathVariable("id") Long customerId
    ) {
        List<VinylRecordSummaryResponseDTO> favoriteRecords = customerFavoritesService.getCustomerFavoriteRecords(customerId);
        return ResponseEntity.ok(favoriteRecords);
    }

    @PostMapping("/{vinylId}")
    public ResponseEntity<Void> addFavoriteRecordToCustomer(
            @PathVariable("id") Long customerId,
            @PathVariable("vinylId") Long vinylRecordId
    ) {
        customerFavoritesService.addFavoriteRecordToCustomer(customerId, vinylRecordId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{vinylId}")
    public ResponseEntity<Void> removeFavoriteRecordFromCustomer(
            @PathVariable("id") Long customerId,
            @PathVariable("vinylId") Long vinylRecordId
    ) {
        customerFavoritesService.removeFavoriteRecordFromCustomer(customerId, vinylRecordId);
        return ResponseEntity.noContent().build();
    }
}
