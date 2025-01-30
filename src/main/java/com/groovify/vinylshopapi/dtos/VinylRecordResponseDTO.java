package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.Genre;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class VinylRecordResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Genre genre;
    private String label;
    private BigDecimal price;
    private LocalDate releaseDate;
    private Duration playTime;
    private Boolean isLimitedEdition;
    private String EAN;
}
