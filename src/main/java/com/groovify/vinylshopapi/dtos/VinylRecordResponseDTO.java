package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinylRecordResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Genre genre;
    private String label;
    private BigDecimal price;
    private LocalDate releaseDate;
    private Long playTimeSeconds;
    private Boolean isLimitedEdition;
    private String ean;
    private ArtistResponseDTO artist;
    private VinylRecordStockResponseDTO stock;
    private VinylRecordCoverResponseDTO cover;
}
