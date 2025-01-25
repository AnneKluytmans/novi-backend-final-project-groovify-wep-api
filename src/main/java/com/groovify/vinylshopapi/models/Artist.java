package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


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
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @AssertTrue(message = "Birth date must be after 1886")
    public boolean birthDateIsValid() {
        if (birthDate == null) {
            return false;
        }
        LocalDate minValidBirthDate = LocalDate.of(1886, 1, 1);
        return birthDate.isAfter(minValidBirthDate);
    }

    @NotBlank(message = "Country of origin cannot be blank")
    @Size(max = 100, message = "Country of origin must not exceed 100 characters")
    private String countryOfOrigin;

    @PositiveOrZero(message = "Popularity score must be ")
    private float popularity;

}
