package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.VinylRecordCoverFileResponseDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordCoverMetadataResponseDTO;
import com.groovify.vinylshopapi.models.VinylRecordCover;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VinylRecordCoverMapper {
    @Mapping(target = "downloadUrl", ignore = true)
    VinylRecordCoverMetadataResponseDTO toMetadataResponseDTO(VinylRecordCover cover);

    VinylRecordCoverFileResponseDTO toFileResponseDTO(VinylRecordCover cover);
}
