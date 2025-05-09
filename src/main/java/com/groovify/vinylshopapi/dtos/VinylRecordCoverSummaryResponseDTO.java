package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinylRecordCoverSummaryResponseDTO {
    private String filename;
    private String fileType;
    private String downloadUrl;
}
