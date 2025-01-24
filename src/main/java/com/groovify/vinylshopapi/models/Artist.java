package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Birth year cannot be null")
    @Min(value = 1900, message = "Year must be after 1900")
    @Max(value = 2025, message = "Year cannot be in the future")
    private Integer birthYear;

    @NotBlank(message = "Country of origin cannot be blank")
    @Size(max = 100, message = "Country of origin must not exceed 100 characters")
    private String countryOfOrigin;

    @PositiveOrZero(message = "Popularity score must be ")
    private float popularity;

}
