package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponseDTO {
    private Long id;
    private String name;
    private Boolean isGroup;
    private LocalDate debutDate;
    private String countryOfOrigin;
    private Integer popularity;
}
