package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.ArtistPatchDTO;
import com.groovify.vinylshopapi.dtos.ArtistRequestDTO;
import com.groovify.vinylshopapi.dtos.ArtistResponseDTO;
import com.groovify.vinylshopapi.models.Artist;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    @Mapping(target = "id", ignore=true)
    Artist toEntity(ArtistRequestDTO artistRequestDTO);

    ArtistResponseDTO toResponseDTO(Artist artist);

    List<ArtistResponseDTO> toResponseDTOs(List<Artist> artists);

    void updateArtist(ArtistRequestDTO artistRequestDTO, @MappingTarget Artist artist);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateArtist(ArtistPatchDTO artistPatchDTO, @MappingTarget Artist artist);
}
