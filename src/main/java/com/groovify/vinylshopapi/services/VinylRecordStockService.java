package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordStockRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordStockResponseDTO;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.VinylRecordStockMapper;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.models.VinylRecordStock;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordStockRepository;
import org.springframework.stereotype.Service;

@Service
public class VinylRecordStockService {

    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordStockRepository vinylRecordStockRepository;
    private final VinylRecordStockMapper vinylRecordStockMapper;

    public VinylRecordStockService(
            VinylRecordRepository vinylRecordRepository,
            VinylRecordStockRepository vinylRecordStockRepository,
            VinylRecordStockMapper vinylRecordStockMapper
    ) {
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordStockRepository = vinylRecordStockRepository;
        this.vinylRecordStockMapper = vinylRecordStockMapper;
    }


    public VinylRecordStockResponseDTO getStock(Long vinylRecordId) {
        return vinylRecordStockMapper.toResponseDTO(findStock(vinylRecordId));
    }

    public VinylRecordStockResponseDTO createStock(Long vinylRecordId, VinylRecordStockRequestDTO stockRequestDTO) {
        VinylRecord vinylRecord = findVinylRecord(vinylRecordId);

        if (vinylRecord.getStock() != null) {
            throw new ConflictException("Stock already exists for vinyl record with id: " + vinylRecordId);
        }

        VinylRecordStock stock = vinylRecordStockMapper.toEntity(stockRequestDTO);
        stock.setVinylRecord(vinylRecord);

        return vinylRecordStockMapper.toResponseDTO(vinylRecordStockRepository.save(stock));
    }

    public VinylRecordStockResponseDTO updateStock(Long vinylRecordId, VinylRecordStockRequestDTO stockRequestDTO) {
        VinylRecordStock stock = findStock(vinylRecordId);

        vinylRecordStockMapper.updateVinylRecordStock(stockRequestDTO, stock);
        return vinylRecordStockMapper.toResponseDTO(vinylRecordStockRepository.save(stock));
    }

    public void deleteStock(Long vinylRecordId) {
        VinylRecord vinylRecord = findVinylRecord(vinylRecordId);
        VinylRecordStock stock = findStock(vinylRecordId);

        vinylRecord.setStock(null);
        vinylRecordRepository.save(vinylRecord);
        vinylRecordStockRepository.delete(stock);
    }


    private VinylRecord findVinylRecord(Long vinylRecordId) {
        return vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("No vinyl record found with id: " + vinylRecordId));
    }

    private VinylRecordStock findStock(Long vinylRecordId) {
        VinylRecord vinylRecord = findVinylRecord(vinylRecordId);
        return vinylRecordStockRepository.findByVinylRecord(vinylRecord)
                .orElseThrow(() -> new RecordNotFoundException("No stock found for vinyl record: " + vinylRecord.getTitle()));
    }

}
