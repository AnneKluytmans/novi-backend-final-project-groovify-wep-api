package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordPatchDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordResponseDTO;
import com.groovify.vinylshopapi.exceptions.DeleteOperationException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.VinylRecordMapper;
import com.groovify.vinylshopapi.models.Artist;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.ArtistRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import com.groovify.vinylshopapi.specifications.VinylRecordSpecification;

import com.groovify.vinylshopapi.utils.SortHelper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VinylRecordService {

    private final ArtistRepository artistRepository;
    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordMapper vinylRecordMapper;

    public VinylRecordService(
            ArtistRepository artistRepository,
            VinylRecordRepository vinylRecordRepository,
            VinylRecordMapper vinylRecordMapper
    ) {
        this.artistRepository = artistRepository;
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordMapper = vinylRecordMapper;
    }

    public List<VinylRecordResponseDTO> getVinylRecords(
            String title,
            String genre,
            String artist,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isLimitedEdition,
            Boolean isAvailable,
            String sortBy,
            String sortOrder,
            Integer limit
    ) {
        Sort sort = getVinylRecordsSort(sortBy, sortOrder);
        Specification<VinylRecord> specification = VinylRecordSpecification.filterVinylRecords(
                title, genre, artist, minPrice, maxPrice, isLimitedEdition, isAvailable
        );
        List<VinylRecord> vinylRecords = vinylRecordRepository.findAll(specification, sort);

        if (limit != null && limit > 0 && limit < vinylRecords.size()) {
            vinylRecords = vinylRecords.subList(0, limit);
        }

        return vinylRecordMapper.toResponseDTOs(vinylRecords);
    }

    public VinylRecordResponseDTO getVinylRecordById(Long id) {
        return vinylRecordMapper.toResponseDTO(findVinylRecord(id));
    }

    public VinylRecordResponseDTO getVinylRecordByTitle(String title) {
        VinylRecord vinylRecord = vinylRecordRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new RecordNotFoundException("No vinyl record found with title: " + title));

        return vinylRecordMapper.toResponseDTO(vinylRecord);
    }

    public VinylRecordResponseDTO createVinylRecord(VinylRecordRequestDTO vinylRecordRequestDTO) {
        VinylRecord vinylRecord = vinylRecordMapper.toEntity(vinylRecordRequestDTO);
        return vinylRecordMapper.toResponseDTO(vinylRecordRepository.save(vinylRecord));
    }

    public VinylRecordResponseDTO updateVinylRecord(Long id, VinylRecordRequestDTO vinylRecordRequestDTO) {
        VinylRecord vinylRecord = findVinylRecord(id);

        vinylRecordMapper.updateVinylRecord(vinylRecordRequestDTO, vinylRecord);
        return vinylRecordMapper.toResponseDTO(vinylRecordRepository.save(vinylRecord));
    }

    public VinylRecordResponseDTO partialUpdateVinylRecord(Long id, VinylRecordPatchDTO vinylRecordPatchDTO) {
        VinylRecord vinylRecord = findVinylRecord(id);

        vinylRecordMapper.partialUpdateVinylRecord(vinylRecordPatchDTO, vinylRecord);
        return vinylRecordMapper.toResponseDTO(vinylRecordRepository.save(vinylRecord));
    }

    public void deleteVinylRecord(Long id) {
        VinylRecord vinylRecord = findVinylRecord(id);

        if (!vinylRecord.getOrderItems().isEmpty()) {
            throw new DeleteOperationException("Vinyl record " + vinylRecord.getTitle() + " cannot be deleted because " +
                    "it is linked to one or more orders");
        }

        for (Customer customer : vinylRecord.getCustomers()) {
            customer.getFavoriteVinylRecords().remove(vinylRecord);
        }

        vinylRecordRepository.deleteById(id);
    }


    public void addArtistToVinyl(Long vinylRecordId, Long artistId) {
        VinylRecord vinylRecord = findVinylRecord(vinylRecordId);

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RecordNotFoundException("No artist found with id: " + artistId));

        vinylRecord.setArtist(artist);
        vinylRecordRepository.save(vinylRecord);
    }

    public void removeArtistFromVinyl(Long vinylRecordId) {
        VinylRecord vinylRecord = findVinylRecord(vinylRecordId);

        vinylRecord.setArtist(null);
        vinylRecordRepository.save(vinylRecord);
    }


    private Sort getVinylRecordsSort(String sortBy, String sortOrder) {
        Sort sort;
        if (sortBy.trim().equalsIgnoreCase("bestselling")) {
            Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
            sort = Sort.by(direction, "stock.amountSold");
        } else {
            sort = SortHelper.getSort(sortBy, sortOrder, List.of("id", "price", "releaseDate", "title"));
        }
        return sort;
    }

    private VinylRecord findVinylRecord(Long vinylRecordId) {
        return vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("No vinyl record found with id: " + vinylRecordId));
    }
}
