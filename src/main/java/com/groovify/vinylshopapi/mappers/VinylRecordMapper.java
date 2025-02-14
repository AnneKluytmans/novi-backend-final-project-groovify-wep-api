package com.groovify.vinylshopapi.mappers;

import com.groovify.vinylshopapi.dtos.VinylRecordRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordResponseDTO;
import com.groovify.vinylshopapi.enums.Genre;
import com.groovify.vinylshopapi.models.VinylRecord;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class, VinylRecordStockMapper.class, VinylRecordCoverMapper.class})
public interface VinylRecordMapper {
    @Mapping(target = "id", ignore=true)
    @Mapping(target = "genre", source = "genre", qualifiedByName = "stringToGenre")
    VinylRecord toEntity(VinylRecordRequestDTO vinylRecordRequestDTO);

    @Mapping(target = "genre", source = "genre", qualifiedByName = "genreToString")
    @Mapping(target = "artist", source = "artist")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "cover", source = "cover")
    VinylRecordResponseDTO toResponseDTO(VinylRecord vinylRecord);

    @Mapping(target = "genre", source = "genre", qualifiedByName = "genreToString")
    @Mapping(target = "artist", source = "artist")
    @Mapping(target = "stock", source = "stock")
    @Mapping(target = "cover", source = "cover")
    List<VinylRecordResponseDTO> toResponseDTOs(List<VinylRecord> vinylRecords);

    @AfterMapping
    default void addDownloadUrl(@MappingTarget VinylRecordResponseDTO vinylRecordResponseDTO, VinylRecord vinylRecord) {
        String downloadUrl = generateCoverDownloadUrl(vinylRecord.getId());
        vinylRecordResponseDTO.getCover().setDownloadUrl(downloadUrl);
    }

    default String generateCoverDownloadUrl(Long vinylRecordId) {
        return "/api/vinyl-records/" + vinylRecordId + "/cover/download";
    }

    @Named("stringToGenre")
    static Genre stringToGenre(String genre) {
        return Genre.stringToGenre(genre);
    }

    @Named("genreToString")
    static String genreToString(Genre genre) {
        return genre != null ? genre.name() : null;
    }
}
