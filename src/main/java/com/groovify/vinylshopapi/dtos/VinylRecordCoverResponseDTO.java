package com.groovify.vinylshopapi.dtos;

import lombok.Data;

@Data
public class VinylRecordCoverResponseDTO {
    private String filename;
    private String fileType;
    private String downloadUrl;
}
