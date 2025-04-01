package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.VinylRecordSummaryResponseDTO;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.VinylRecordMapper;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerFavoritesService {

    private final CustomerRepository customerRepository;
    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordMapper vinylRecordMapper;

    public CustomerFavoritesService(
            CustomerRepository customerRepository,
            VinylRecordRepository vinylRecordRepository,
            VinylRecordMapper vinylRecordMapper
    ) {
        this.customerRepository = customerRepository;
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordMapper = vinylRecordMapper;
    }

    public List<VinylRecordSummaryResponseDTO> getCustomerFavoriteRecords(Long customerId) {
        List<VinylRecord> favoriteRecords = findCustomer(customerId).getFavoriteVinylRecords();
        return vinylRecordMapper.toSummaryResponseDTOs(favoriteRecords);
    }

    public void addFavoriteRecordToCustomer(Long customerId, Long vinylRecordId) {
        Customer customer = findCustomer(customerId);
        VinylRecord favoriteRecord = findVinylRecord(vinylRecordId);

        if (customer.getFavoriteVinylRecords().contains(favoriteRecord)) {
            throw new ConflictException("Vinyl record with id: '" + vinylRecordId + "' is already in customer's favorite list.");
        }

        customer.getFavoriteVinylRecords().add(favoriteRecord);
        customerRepository.save(customer);
    }

    public void removeFavoriteRecordFromCustomer(Long customerId, Long vinylRecordId) {
        Customer customer = findCustomer(customerId);
        VinylRecord favoriteRecord = findVinylRecord(vinylRecordId);

        if (!customer.getFavoriteVinylRecords().contains(favoriteRecord)) {
            throw new ConflictException("No vinyl record found in the customer's favorite list with id: " + vinylRecordId);
        }

        customer.getFavoriteVinylRecords().remove(favoriteRecord);
        customerRepository.save(customer);
    }


    private Customer findCustomer(Long customerId) {
        return customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("No customer found with id: " + customerId));
    }

    private VinylRecord findVinylRecord(Long vinylRecordId) {
        return vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("No vinyl record found with id: " + vinylRecordId));
    }
}
