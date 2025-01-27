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

    ArtistResponseDTO toResponseDTO(Artist artist);

    List<ArtistResponseDTO> toResponseDTOs(List<Artist> artists);
}
