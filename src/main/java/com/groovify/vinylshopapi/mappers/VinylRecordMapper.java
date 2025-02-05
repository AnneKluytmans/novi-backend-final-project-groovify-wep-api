package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.VinylRecordRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordResponseDTO;
import com.groovify.vinylshopapi.enums.Genre;
import com.groovify.vinylshopapi.models.VinylRecord;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = ArtistMapper.class)
public interface VinylRecordMapper {
    @Mapping(target = "id", ignore=true)
    @Mapping(target = "genre", source = "genre", qualifiedByName = "stringToGenre")
    VinylRecord toEntity(VinylRecordRequestDTO vinylRecordRequestDTO);

    @Mapping(target = "genre", source = "genre", qualifiedByName = "genreToString")
    @Mapping(target = "artist", source = "artist")
    VinylRecordResponseDTO toResponseDTO(VinylRecord vinylRecord);

    @Mapping(target = "genre", source = "genre", qualifiedByName = "genreToString")
    @Mapping(target = "artist", source = "artist")
    List<VinylRecordResponseDTO> toResponseDTOs(List<VinylRecord> vinylRecords);


    @Named("stringToGenre")
    static Genre stringToGenre(String genre) {
        return Genre.stringToGenre(genre);
    }

    @Named("genreToString")
    static String genreToString(Genre genre) {
        return genre != null ? genre.name() : null;
    }
}
