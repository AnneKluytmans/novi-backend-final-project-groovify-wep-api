package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.VinylRecordStockRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordStockResponseDTO;
import com.groovify.vinylshopapi.models.VinylRecordStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VinylRecordStockMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vinylRecord", ignore = true)
    VinylRecordStock toEntity(VinylRecordStockRequestDTO stockRequestDTO);

    VinylRecordStockResponseDTO toResponseDTO(VinylRecordStock stock);
}
