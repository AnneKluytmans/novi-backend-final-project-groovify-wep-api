package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.enums.Genre;
import com.groovify.vinylshopapi.models.VinylRecord;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ArtistMapper.class,
                VinylRecordStockMapper.class,
                VinylRecordCoverMapper.class
})
public interface VinylRecordMapper {
    @Mapping(target = "id", ignore=true)
    VinylRecord toEntity(VinylRecordRequestDTO vinylRecordRequestDTO);

    @Mapping(target = "artist", source = "artist")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "cover", source = "cover")
    VinylRecordResponseDTO toResponseDTO(VinylRecord vinylRecord);

    List<VinylRecordResponseDTO> toResponseDTOs(List<VinylRecord> vinylRecords);

    VinylRecordSummaryResponseDTO toSummaryResponseDTO(VinylRecord vinylRecord);

    List<VinylRecordSummaryResponseDTO> toSummaryResponseDTOs(List<VinylRecord> vinylRecords);

    void updateVinylRecord(VinylRecordRequestDTO vinylRecordRequestDTO, @MappingTarget VinylRecord vinylRecord);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateVinylRecord(VinylRecordPatchDTO vinylRecordPatchDTO, @MappingTarget VinylRecord vinylRecord);
}
