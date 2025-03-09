package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.ArtistPatchDTO;
import com.groovify.vinylshopapi.dtos.ArtistRequestDTO;
import com.groovify.vinylshopapi.dtos.ArtistResponseDTO;
import com.groovify.vinylshopapi.services.ArtistService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minPopularity,
            @RequestParam(required = false) Integer maxPopularity,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) Integer limit)
    {
        List<ArtistResponseDTO> artists = artistService.getArtists(
                country, name, minPopularity, maxPopularity, sortBy, sortOrder, limit
        );
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponseDTO> getArtistById(
            @PathVariable Long id
    ) {
        ArtistResponseDTO artist = artistService.getArtistById(id);
        return ResponseEntity.ok(artist);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ArtistResponseDTO> getArtistByName(
            @PathVariable String name
    ) {
        ArtistResponseDTO artist = artistService.getArtistByName(name);
        return ResponseEntity.ok(artist);
    }

    @PostMapping
    public ResponseEntity<?> createArtist(
            @Valid @RequestBody ArtistRequestDTO artistRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        ArtistResponseDTO newArtist = artistService.createArtist(artistRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newArtist.getId())
                .toUri();
        return ResponseEntity.created(location).body(newArtist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtist(
            @PathVariable Long id,
            @Valid @RequestBody ArtistRequestDTO artistRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        ArtistResponseDTO updatedArtist = artistService.updateArtist(id, artistRequestDTO);
        return ResponseEntity.ok(updatedArtist);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateArtist(
            @PathVariable Long id,
            @Valid @RequestBody ArtistPatchDTO artistPatchDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        ArtistResponseDTO updatedArtist = artistService.partialUpdateArtist(id, artistPatchDTO);
        return ResponseEntity.ok(updatedArtist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(
            @PathVariable Long id
    ) {
        artistService.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }
}
