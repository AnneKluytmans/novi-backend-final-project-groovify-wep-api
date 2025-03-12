package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinylRecordSummaryResponseDTO {
    private Long id;
    private String title;
    private Genre genre;
}
