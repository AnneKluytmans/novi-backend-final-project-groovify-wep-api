package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.Genre;
import com.groovify.vinylshopapi.validation.ValidDate;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VinylRecordPatchDTO {
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private String genre;

    @Size(max = 100, message = "Label must not exceed 100 characters")
    private String label;

    @DecimalMin(value = "0.01", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @ValidDate(min = "1900-01-01", max = "now+1Y", message = "Release date must be between 1900 and a year from now")
    private LocalDate releaseDate;

    @Positive(message = "Play time must be greater than 0 seconds")
    private Long playTime;

    private Boolean isLimitedEdition;

    @Pattern(regexp = "\\d{13}", message = "EAN must be a valid 13-digit number")
    private String EAN;
}
