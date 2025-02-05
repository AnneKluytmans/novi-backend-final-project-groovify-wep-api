package com.groovify.vinylshopapi.models;

import com.groovify.vinylshopapi.validation.ValidDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Type is required")
    private Boolean isGroup = false;

    @NotNull(message = "Debut date is required")
    @ValidDate(min = "1500-01-01", mustBePast = true, message = "Debut date must be between 1500 and now")
    private LocalDate debutDate;

    @NotBlank(message = "Country of origin cannot be blank")
    @Size(max = 100, message = "Country of origin must not exceed 100 characters")
    private String countryOfOrigin;

    @NotNull(message = "Popularity score is required")
    @Min(value = 0, message = "Popularity score must be between 0 and 100")
    @Max(value = 100, message = "Popularity score must be between 0 and 100")
    private Integer popularity = 0;

    @OneToMany(mappedBy = "artist")
    private List<VinylRecord> vinylRecords = new ArrayList<>();

}
