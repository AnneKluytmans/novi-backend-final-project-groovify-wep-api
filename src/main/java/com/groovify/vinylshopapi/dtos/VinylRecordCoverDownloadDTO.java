package com.groovify.vinylshopapi.dtos;

import lombok.Data;

@Data
public class VinylRecordCoverDownloadDTO {
    private String filename;
    private String fileType;
    private byte[] data;
}
