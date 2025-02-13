package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.VinylRecordCoverFileResponseDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordCoverMetadataResponseDTO;
import com.groovify.vinylshopapi.models.VinylRecordCover;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VinylRecordCoverMapper {
    VinylRecordCoverMetadataResponseDTO toMetadataResponseDTO(VinylRecordCover cover);
    VinylRecordCoverFileResponseDTO toFileResponseDTO(VinylRecordCover cover);
}
