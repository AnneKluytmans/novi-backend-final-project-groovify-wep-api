package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordStockPatchDTO;
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

    private final VinylRecordStockRepository vinylRecordStockRepository;
    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordStockMapper vinylRecordStockMapper;

    public VinylRecordStockService(VinylRecordStockRepository vinylRecordStockRepository, VinylRecordRepository vinylRecordRepository, VinylRecordStockMapper vinylRecordStockMapper) {
        this.vinylRecordStockRepository = vinylRecordStockRepository;
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordStockMapper = vinylRecordStockMapper;
    }


    public VinylRecordStockResponseDTO getStockByVinylRecord(Long vinylRecordId) {
        VinylRecord vinylRecord = vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + vinylRecordId + " not found"));

        VinylRecordStock stock = vinylRecordStockRepository.findByVinylRecord(vinylRecord)
                .orElseThrow(() -> new RecordNotFoundException("Stock from vinyl record " + vinylRecord.getTitle() + " not found"));

        return vinylRecordStockMapper.toResponseDTO(stock);
    }

    public VinylRecordStockResponseDTO createStock(VinylRecordStockRequestDTO stockRequestDTO) {
        VinylRecord vinylRecord = vinylRecordRepository.findById(stockRequestDTO.getVinylRecordId())
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + stockRequestDTO.getVinylRecordId() + " not found"));

        if (vinylRecord.getStock() != null) {
            throw new ConflictException("Stock already exists for vinyl record with id " + stockRequestDTO.getVinylRecordId());
        }

        VinylRecordStock stock = vinylRecordStockMapper.toEntity(stockRequestDTO);
        stock.setVinylRecord(vinylRecord);

        VinylRecordStock savedStock = vinylRecordStockRepository.save(stock);

        return vinylRecordStockMapper.toResponseDTO(savedStock);
    }

    public VinylRecordStockResponseDTO updateStock(Long id, VinylRecordStockPatchDTO stockPatchDTO) {
        VinylRecordStock stock = vinylRecordStockRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Stock with id " + id + " not found"));

        stock.setAmountInStock(stockPatchDTO.getAmountInStock());
        stock.setAmountSold(stockPatchDTO.getAmountSold());

        VinylRecordStock savedStock = vinylRecordStockRepository.save(stock);
        return vinylRecordStockMapper.toResponseDTO(savedStock);
    }

    public void deleteStock(Long id) {
        VinylRecordStock stock = vinylRecordStockRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Stock with id " + id + " not found"));

        VinylRecord vinylRecord = stock.getVinylRecord();
        vinylRecord.setStock(null);

        vinylRecordStockRepository.delete(stock);
    }

}
