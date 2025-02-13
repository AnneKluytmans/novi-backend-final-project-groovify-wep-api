package com.groovify.vinylshopapi.dtos;

import lombok.Data;

@Data
public class VinylRecordCoverMetadataResponseDTO {
    private Long id;
    private String filename;
    private String fileType;
    private String downloadUrl;
}
