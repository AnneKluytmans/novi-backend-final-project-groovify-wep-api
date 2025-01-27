package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.ArtistRequestDTO;
import com.groovify.vinylshopapi.dtos.ArtistResponseDTO;
import com.groovify.vinylshopapi.models.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    @Mapping(target = "id", ignore=true)
    Artist toEntity(ArtistRequestDTO artistRequestDTO);

    @Mapping(target = "yearsSinceDebut", ignore = true)
    ArtistResponseDTO toResponseDTO(Artist artist);

    @Mapping(target = "yearsSinceDebut", ignore = true)
    List<ArtistResponseDTO> toResponseDTOs(List<Artist> artists);
}
