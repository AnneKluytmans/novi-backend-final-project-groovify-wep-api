package com.groovify.vinylshopapi.dtos;

import lombok.Data;

@Data
public class VinylRecordCoverMetadataResponseDTO {
    private String filename;
    private String fileType;
    private String downloadUrl;
}
