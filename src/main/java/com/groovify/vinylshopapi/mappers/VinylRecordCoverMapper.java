package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.VinylRecordCoverResponseDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordCoverSummaryResponseDTO;
import com.groovify.vinylshopapi.models.VinylRecordCover;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VinylRecordCoverMapper {
    VinylRecordCoverResponseDTO toResponseDTO(VinylRecordCover cover);

    VinylRecordCoverSummaryResponseDTO toSummaryResponseDTO(VinylRecordCover cover);
}
