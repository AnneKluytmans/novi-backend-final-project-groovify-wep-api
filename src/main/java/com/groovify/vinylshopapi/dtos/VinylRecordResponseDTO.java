package com.groovify.vinylshopapi.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VinylRecordResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String label;
    private BigDecimal price;
    private LocalDate releaseDate;
    private Long playTimeSeconds;
    private Boolean isLimitedEdition;
    private String ean;
    private ArtistResponseDTO artist;
    private VinylRecordStockResponseDTO stock;
    private VinylRecordCoverMetadataResponseDTO cover;
}
