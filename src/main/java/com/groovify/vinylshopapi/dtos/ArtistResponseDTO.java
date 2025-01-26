package com.groovify.vinylshopapi.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ArtistResponseDTO {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private Integer age;
    private String countryOfOrigin;
    private Integer popularity;
}
