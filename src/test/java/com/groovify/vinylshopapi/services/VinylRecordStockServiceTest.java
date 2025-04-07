package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordStockRequestDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordStockResponseDTO;
import com.groovify.vinylshopapi.enums.Genre;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.VinylRecordStockMapper;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.models.VinylRecordStock;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VinylRecordStockServiceTest {
    @Mock
    private VinylRecordRepository vinylRecordRepository;
    @Mock
    private VinylRecordStockRepository vinylRecordStockRepository;
    @Mock
    private VinylRecordStockMapper vinylRecordStockMapper;

    @InjectMocks
    private VinylRecordStockService vinylRecordStockService;

    private List<VinylRecord> vinylRecords;
    private VinylRecord vinylRecordWithoutStock;
    private List<VinylRecordStock> vinylRecordsStock;
    private VinylRecordStockRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        vinylRecords = List.of(
                new VinylRecord("Abbey Road", "The Beatles", Genre.ROCK, "Apple", new BigDecimal("29.99"),
                        LocalDate.of(1969, 9, 26), 4200L, true, "1234567890123"),
                new VinylRecord("Rumours", "Fleetwood Mac", Genre.ROCK, "Warner", new BigDecimal("25.00"),
                        LocalDate.of(1977, 2, 4), 3900L, true, "2345678901234"),
                new VinylRecord("Arrival", "ABBA", Genre.POP, "Polar", new BigDecimal("22.99"),
                        LocalDate.of(1976, 10, 11), 3300L, false, "3456789012345")
        );

        vinylRecordWithoutStock = new VinylRecord(
                "Thriller", "Michael Jackson", Genre.POP, "Epic",
                new BigDecimal("35.00"), LocalDate.of(1982, 11, 30),
                5000L, true, "9876543210987"
        );

        vinylRecordsStock = List.of(
                new VinylRecordStock(15, 5, vinylRecords.get(0)),
                new VinylRecordStock(20, 8, vinylRecords.get(1)),
                new VinylRecordStock(30, 10, vinylRecords.get(2))
        );

        for (int i = 0; i < vinylRecords.size(); i++) {
            vinylRecords.get(i).setStock(vinylRecordsStock.get(i));
        }

        requestDTO = new VinylRecordStockRequestDTO(25, 0);
    }

    @Test
    void givenExistingStock_whenGetStock_thenReturnStock() throws Exception {
        VinylRecord record = vinylRecords.get(0);
        VinylRecordStock stock = record.getStock();

        when(vinylRecordRepository.findById(record.getId())).thenReturn(Optional.of(record));
        when(vinylRecordStockRepository.findByVinylRecord(record)).thenReturn(Optional.of(stock));
        when(vinylRecordStockMapper.toResponseDTO(stock)).thenReturn(new VinylRecordStockResponseDTO(
                stock.getId(), stock.getAmountInStock(), stock.getAmountSold()
        ));

        VinylRecordStockResponseDTO result = vinylRecordStockService.getStock(record.getId());

        assertNotNull(result);
        assertEquals(stock.getId(), result.getId());
        assertEquals(stock.getAmountInStock(), result.getAmountInStock());
        assertEquals(stock.getAmountSold(), result.getAmountSold());
    }

    @Test
    void givenNotExistingVinylRecord_whenGetStock_thenThrowRecordNotFoundException() throws Exception {
        when(vinylRecordRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> vinylRecordStockService.getStock(999L));
    }

    @Test
    void givenNotExistingStock_whenGetStock_thenThrowRecordNotFoundException() throws Exception {
        when(vinylRecordRepository.findById(vinylRecordWithoutStock.getId())).thenReturn(Optional.of(vinylRecordWithoutStock));
        when(vinylRecordStockRepository.findByVinylRecord(vinylRecordWithoutStock)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> vinylRecordStockService.getStock(vinylRecordWithoutStock.getId()));
    }

    @Test
    void givenNotExistingStock_whenCreateStock_thenReturnCreatedStock() throws Exception {
        VinylRecordStock newStock = new VinylRecordStock(25, 0, vinylRecordWithoutStock);

        when(vinylRecordRepository.findById(vinylRecordWithoutStock.getId())).thenReturn(Optional.of(vinylRecordWithoutStock));
        when(vinylRecordStockMapper.toEntity(requestDTO)).thenReturn(newStock);
        when(vinylRecordStockRepository.save(newStock)).thenReturn(newStock);
        when(vinylRecordStockMapper.toResponseDTO(newStock)).thenReturn(new VinylRecordStockResponseDTO(
                newStock.getId(), newStock.getAmountInStock(), newStock.getAmountSold()
        ));

        VinylRecordStockResponseDTO result = vinylRecordStockService.createStock(vinylRecordWithoutStock.getId(), requestDTO);

        assertNotNull(result);
        assertEquals(newStock.getId(), result.getId());
        assertEquals(newStock.getAmountInStock(), result.getAmountInStock());
        assertEquals(newStock.getAmountSold(), result.getAmountSold());
    }

    @Test
    void givenExistingStock_whenCreateStock_thenThrowConflictException() throws Exception {
        VinylRecord record = vinylRecords.get(0);

        when(vinylRecordRepository.findById(record.getId())).thenReturn(Optional.of(record));

        assertThrows(ConflictException.class, () -> vinylRecordStockService.createStock(record.getId(), requestDTO));
    }

    @Test
    void givenNotExistingVinylRecord_whenCreateStock_thenThrowRecordNotFoundException() throws Exception {
        when(vinylRecordRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> vinylRecordStockService.createStock(999L, requestDTO));
    }

    @Test
    void givenExistingStock_whenUpdateStock_thenReturnUpdatedStock() throws Exception {
        VinylRecord record = vinylRecords.get(0);
        VinylRecordStock stock = record.getStock();

        when(vinylRecordRepository.findById(record.getId())).thenReturn(Optional.of(record));
        when(vinylRecordStockRepository.findByVinylRecord(record)).thenReturn(Optional.of(stock));
        doAnswer(invocation -> {
            VinylRecordStockRequestDTO dto = invocation.getArgument(0);
            VinylRecordStock target = invocation.getArgument(1);
            target.setAmountInStock(dto.getAmountInStock());
            target.setAmountSold(dto.getAmountSold());
            return null;
        }).when(vinylRecordStockMapper).updateVinylRecordStock(requestDTO, stock);
        when(vinylRecordStockRepository.save(stock)).thenReturn(stock);
        when(vinylRecordStockMapper.toResponseDTO(stock)).thenReturn(new VinylRecordStockResponseDTO(
                stock.getId(), requestDTO.getAmountInStock(), requestDTO.getAmountSold()
        ));

        VinylRecordStockResponseDTO result = vinylRecordStockService.updateStock(record.getId(), requestDTO);

        assertNotNull(result);
        assertEquals(stock.getId(), result.getId());
        assertEquals(requestDTO.getAmountInStock(), result.getAmountInStock());
        assertEquals(requestDTO.getAmountSold(), result.getAmountSold());
    }

    @Test
    void givenNotExistingVinylRecord_whenUpdateStock_thenThrowRecordNotFoundException() throws Exception {
        when(vinylRecordRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> vinylRecordStockService.updateStock(999L, requestDTO));
    }

    @Test
    void givenNotExistingStock_whenUpdateStock_thenThrowRecordNotFoundException() throws Exception {
        when(vinylRecordRepository.findById(vinylRecordWithoutStock.getId())).thenReturn(Optional.of(vinylRecordWithoutStock));
        when(vinylRecordStockRepository.findByVinylRecord(vinylRecordWithoutStock)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> vinylRecordStockService.updateStock(vinylRecordWithoutStock.getId(), requestDTO));
    }

    @Test
    void givenExistingStock_whenDeleteStock_thenDeleteStock() throws Exception {
        VinylRecord record = vinylRecords.get(0);
        VinylRecordStock stock = record.getStock();

        when(vinylRecordRepository.findById(record.getId())).thenReturn(Optional.of(record));
        when(vinylRecordStockRepository.findByVinylRecord(record)).thenReturn(Optional.of(stock));
        doNothing().when(vinylRecordStockRepository).delete(stock);

        vinylRecordStockService.deleteStock(record.getId());

        assertNull(record.getStock());
        verify(vinylRecordRepository).save(record);
        verify(vinylRecordStockRepository).delete(stock);
    }

    @Test
    void givenNotExistingVinylRecord_whenDeleteStock_thenThrowRecordNotFoundException() throws Exception {
        when(vinylRecordRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> vinylRecordStockService.deleteStock(999L));
    }

    @Test
    void givenNotExistingStock_whenDeleteStock_thenThrowRecordNotFoundException() throws Exception {
        when(vinylRecordRepository.findById(vinylRecordWithoutStock.getId())).thenReturn(Optional.of(vinylRecordWithoutStock));
        when(vinylRecordStockRepository.findByVinylRecord(vinylRecordWithoutStock)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> vinylRecordStockService.deleteStock(vinylRecordWithoutStock.getId()));
    }
}
