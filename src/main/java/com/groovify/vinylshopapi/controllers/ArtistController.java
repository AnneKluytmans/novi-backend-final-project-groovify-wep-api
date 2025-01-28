package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.ArtistResponseDTO;
import com.groovify.vinylshopapi.services.ArtistService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponseDTO>> getArtists(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer minPopularity,
            @RequestParam(required = false) Integer maxPopularity,
            @RequestParam(defaultValue = "name") String orderBy,
            @RequestParam(defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) Integer limit)
    {
        List<ArtistResponseDTO> artists = artistService.getArtists(country, minPopularity, maxPopularity, orderBy, sortOrder, limit);
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponseDTO> getArtistById(@PathVariable Long id) {
        ArtistResponseDTO artist = artistService.getArtistById(id);
        return ResponseEntity.ok(artist);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ArtistResponseDTO> getArtistByName(@PathVariable String name) {
        ArtistResponseDTO artist = artistService.getArtistByName(name);
        return ResponseEntity.ok(artist);
    }
}
