package com.groovify.vinylshopapi.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ArtistResponseDTO {
    private Long id;
    private String name;
    private Boolean isGroup;
    private LocalDate debutDate;
    private Integer yearsSinceDebut;
    private String countryOfOrigin;
    private Integer popularity;
}
