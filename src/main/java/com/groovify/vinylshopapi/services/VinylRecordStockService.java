package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordStockRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordStockResponseDTO;
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

    public VinylRecordStockResponseDTO createStock(VinylRecordStockRequestDTO vinylRecordStockRequestDTO) {
        VinylRecordStock stock = vinylRecordStockMapper.toEntity(vinylRecordStockRequestDTO);

        VinylRecord vinylRecord = vinylRecordRepository.findById(vinylRecordStockRequestDTO.getVinylRecordId())
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + vinylRecordStockRequestDTO.getVinylRecordId() + " not found"));

        stock.setVinylRecord(vinylRecord);
        vinylRecord.setStock(stock);

        vinylRecordRepository.save(vinylRecord);
        VinylRecordStock savedStock = vinylRecordStockRepository.save(stock);

        return vinylRecordStockMapper.toResponseDTO(savedStock);
    }

    public VinylRecordStockResponseDTO updateStock(Long id, VinylRecordStockRequestDTO vinylRecordStockRequestDTO) {
        VinylRecordStock stock = vinylRecordStockRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Stock with id " + id + " not found"));

        stock.setAmountInStock(vinylRecordStockRequestDTO.getAmountInStock());
        stock.setAmountSold(vinylRecordStockRequestDTO.getAmountSold());

        VinylRecordStock savedStock = vinylRecordStockRepository.save(stock);
        return vinylRecordStockMapper.toResponseDTO(savedStock);
    }

    public void deleteStock(Long id) {
        VinylRecordStock stock = vinylRecordStockRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Stock with id " + id + " not found"));

        VinylRecord vinylRecord = stock.getVinylRecord();
        if (vinylRecord != null) {
            vinylRecord.setStock(null);
            vinylRecordRepository.save(vinylRecord);
        }

        vinylRecordStockRepository.delete(stock);
    }

}
