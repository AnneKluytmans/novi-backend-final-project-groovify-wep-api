package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.validation.ValidDate;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ArtistPatchDTO {
    @Size(min = 1, max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

    private Boolean isGroup;

    @ValidDate(min = "1500-01-01", mustBePast = true, message = "Debut date must be between 1500 and now")
    private LocalDate debutDate;

    @Size(min = 1, max = 100, message = "Country of origin cannot be longer than 100 characters")
    private String countryOfOrigin;

    @Min(value = 0, message = "Popularity score must be between 0 and 100")
    @Max(value = 100, message = "Popularity score must be between 0 and 100")
    private Integer popularity;
}
