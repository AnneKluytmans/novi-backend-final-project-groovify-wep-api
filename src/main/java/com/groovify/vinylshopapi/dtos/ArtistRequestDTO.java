package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.validation.ValidDate;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ArtistRequestDTO {
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Type is required")
    private Boolean isGroup = false;

    @NotNull(message = "Birth date is required")
    @ValidDate(min = "1886-01-01", mustBePast = true, message = "Birth date must be between 1886 and now")
    private LocalDate firstReleaseDate;

    @NotBlank(message = "Country of origin cannot be blank")
    @Size(max = 100, message = "Country of origin must not exceed 100 characters")
    private String countryOfOrigin;

    @NotNull(message = "Popularity score is required")
    @PositiveOrZero(message = "Popularity score cannot be negative")
    private Integer popularity = 0;
}
