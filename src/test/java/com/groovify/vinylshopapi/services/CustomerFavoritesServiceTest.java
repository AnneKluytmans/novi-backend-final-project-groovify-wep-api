package com.groovify.vinylshopapi.services;


import com.groovify.vinylshopapi.dtos.VinylRecordSummaryResponseDTO;
import com.groovify.vinylshopapi.enums.Genre;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.VinylRecordMapper;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
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
public class CustomerFavoritesServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VinylRecordRepository vinylRecordRepository;

    @Mock
    private VinylRecordMapper vinylRecordMapper;

    @InjectMocks
    private CustomerFavoritesService customerFavoritesService;

    private List<VinylRecord> vinylRecords;
    private List<Customer> customers;

    @BeforeEach
    void setUp() {
        vinylRecords = List.of(
                new VinylRecord("Abbey Road", "The Beatles", Genre.ROCK, "Apple", new BigDecimal("29.99"),
                        LocalDate.of(1969, 9, 26), 4200L, true, "1234567890123"),
                new VinylRecord("Rumours", "Fleetwood Mac", Genre.ROCK, "Warner", new BigDecimal("25.00"),
                        LocalDate.of(1977, 2, 4), 3900L, true, "2345678901234"),
                new VinylRecord("Arrival", "ABBA", Genre.POP, "Polar", new BigDecimal("22.99"),
                        LocalDate.of(1976, 10, 11), 3300L, false, "3456789012345"),
                new VinylRecord("Thriller", "Michael Jackson", Genre.POP, "Epic", new BigDecimal("35.00"),
                        LocalDate.of(1982, 11, 30), 5000L, true, "9876543210987")
        );

        customers = List.of(
                new Customer(
                        "john_doe", "john@example.com", "password123", "John", "Doe",
                        LocalDate.of(2003, 5, 15), "0612345678", true
                ),
                new Customer(
                        "jane_smith", "jane@example.com", "password456", "Jane", "Smith",
                        LocalDate.of(1985, 8, 22), "0698765432", false
                ),
                new Customer(
                        "bob_brown", "bob@example.com", "password789", "Bob", "Brown",
                        LocalDate.of(1999, 12, 5), "0687654321", true
                )
        );

        customers.getFirst().getFavoriteVinylRecords().addAll(List.of(vinylRecords.get(0), vinylRecords.get(1)));
        customers.get(1).getFavoriteVinylRecords().add(vinylRecords.get(2));
    }

    @Test
    void givenExistingCustomer_whenGetCustomerFavoriteRecords_thenReturnFavoriteRecords() {
        Customer customer = customers.getFirst();
        List<VinylRecord> favorites = customer.getFavoriteVinylRecords();

        when(customerRepository.findByIdAndIsDeletedFalse(customer.getId())).thenReturn(Optional.of(customer));
        when(vinylRecordMapper.toSummaryResponseDTOs(favorites)).thenReturn(List.of(
                new VinylRecordSummaryResponseDTO(favorites.get(0).getId(), favorites.get(0).getTitle(), favorites.get(0).getGenre()),
                new VinylRecordSummaryResponseDTO(favorites.get(1).getId(), favorites.get(1).getTitle(), favorites.get(1).getGenre())
        ));

        List<VinylRecordSummaryResponseDTO> result = customerFavoritesService.getCustomerFavoriteRecords(customer.getId());

        assertEquals(2, result.size());
        assertEquals(favorites.get(0).getId(), result.get(0).getId());
        assertEquals(favorites.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(favorites.get(0).getGenre(), result.get(0).getGenre());
        assertEquals(favorites.get(1).getId(), result.get(1).getId());
        assertEquals(favorites.get(1).getTitle(), result.get(1).getTitle());
        assertEquals(favorites.get(1).getGenre(), result.get(1).getGenre());

        verify(customerRepository).findByIdAndIsDeletedFalse(customer.getId());
        verify(vinylRecordMapper).toSummaryResponseDTOs(customer.getFavoriteVinylRecords());
    }

    @Test
    void givenExistingCustomerWithoutFavorites_whenGetCustomerFavoriteRecords_thenReturnEmptyList() {
        Customer customer = customers.getLast();
        List<VinylRecord> favorites = customer.getFavoriteVinylRecords();

        when(customerRepository.findByIdAndIsDeletedFalse(customer.getId())).thenReturn(Optional.of(customer));
        when(vinylRecordMapper.toSummaryResponseDTOs(favorites)).thenReturn(List.of());

        List<VinylRecordSummaryResponseDTO> result = customerFavoritesService.getCustomerFavoriteRecords(customer.getId());

        assertTrue(result.isEmpty());
        verify(customerRepository).findByIdAndIsDeletedFalse(customer.getId());
        verify(vinylRecordMapper).toSummaryResponseDTOs(favorites);
    }

    @Test
    void givenNonExistingCustomer_whenGetCustomerFavoriteRecords_thenThrowRecordNotFoundException() {
        when(customerRepository.findByIdAndIsDeletedFalse(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> customerFavoritesService.getCustomerFavoriteRecords(999L));
        verify(customerRepository).findByIdAndIsDeletedFalse(999L);
    }

    @Test
    void givenNewFavorite_whenAddFavoriteRecordToCustomer_thenAddFavoriteSuccessfully() {
        Customer customer = customers.getFirst();
        VinylRecord newFavorite = vinylRecords.get(3);

        when(customerRepository.findByIdAndIsDeletedFalse(customer.getId())).thenReturn(Optional.of(customer));
        when(vinylRecordRepository.findById(newFavorite.getId())).thenReturn(Optional.of(newFavorite));

        customerFavoritesService.addFavoriteRecordToCustomer(customer.getId(), newFavorite.getId());

        assertTrue(customer.getFavoriteVinylRecords().contains(newFavorite));
        verify(customerRepository).save(customer);
        verify(customerRepository).findByIdAndIsDeletedFalse(customer.getId());
        verify(vinylRecordRepository).findById(newFavorite.getId());
    }

    @Test
    void givenExistingFavorite_whenAddFavoriteRecordToCustomer_thenThrowConflictException() {
        Customer customer = customers.getFirst();
        VinylRecord newFavorite = vinylRecords.get(1);

        when(customerRepository.findByIdAndIsDeletedFalse(customer.getId())).thenReturn(Optional.of(customer));
        when(vinylRecordRepository.findById(newFavorite.getId())).thenReturn(Optional.of(newFavorite));

        assertThrows(ConflictException.class, () -> customerFavoritesService.addFavoriteRecordToCustomer(customer.getId(), newFavorite.getId()));
        verify(customerRepository).findByIdAndIsDeletedFalse(customer.getId());
        verify(vinylRecordRepository).findById(newFavorite.getId());
    }

    @Test
    void givenNonExistingCustomer_whenAddFavoriteRecordToCustomer_thenThrowRecordNotFoundException() {
        when(customerRepository.findByIdAndIsDeletedFalse(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> customerFavoritesService.addFavoriteRecordToCustomer(999L, 1L));
        verify(customerRepository).findByIdAndIsDeletedFalse(999L);
    }

    @Test
    void givenNonExistingRecord_whenAddFavoriteRecordToCustomer_thenThrowRecordNotFoundException() {
        Customer customer = customers.getFirst();

        when(customerRepository.findByIdAndIsDeletedFalse(customer.getId())).thenReturn(Optional.of(customer));
        when(vinylRecordRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> customerFavoritesService.addFavoriteRecordToCustomer(customer.getId(), 999L));
        verify(customerRepository).findByIdAndIsDeletedFalse(customer.getId());
        verify(vinylRecordRepository).findById(999L);
    }

    @Test
    void givenExistingFavorite_whenRemoveFavoriteRecordFromCustomer_thenRemoveFavoriteSuccessfully() {
        Customer customer = customers.getFirst();
        VinylRecord existingFavorite = vinylRecords.get(1);

        when(customerRepository.findByIdAndIsDeletedFalse(customer.getId())).thenReturn(Optional.of(customer));
        when(vinylRecordRepository.findById(existingFavorite.getId())).thenReturn(Optional.of(existingFavorite));

        customerFavoritesService.removeFavoriteRecordFromCustomer(customer.getId(), existingFavorite.getId());

        assertFalse(customer.getFavoriteVinylRecords().contains(existingFavorite));
        verify(customerRepository).save(customer);
        verify(customerRepository).findByIdAndIsDeletedFalse(customer.getId());
        verify(vinylRecordRepository).findById(existingFavorite.getId());
    }

    @Test
    void givenNonExistingFavorite_whenRemoveFavoriteRecordFromCustomer_thenThrowConflictException() {
        Customer customer = customers.getFirst();
        VinylRecord nonExistingFavorite = vinylRecords.get(3);

        when(customerRepository.findByIdAndIsDeletedFalse(customer.getId())).thenReturn(Optional.of(customer));
        when(vinylRecordRepository.findById(nonExistingFavorite.getId())).thenReturn(Optional.of(nonExistingFavorite));

        assertThrows(ConflictException.class, () -> customerFavoritesService.removeFavoriteRecordFromCustomer(customer.getId(), nonExistingFavorite.getId()));
        verify(customerRepository).findByIdAndIsDeletedFalse(customer.getId());
        verify(vinylRecordRepository).findById(nonExistingFavorite.getId());
    }

    @Test
    void givenNonExistingCustomer_whenRemoveFavoriteRecordFromCustomer_thenThrowRecordNotFoundException() {
        when(customerRepository.findByIdAndIsDeletedFalse(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> customerFavoritesService.removeFavoriteRecordFromCustomer(999L, 1L));
        verify(customerRepository).findByIdAndIsDeletedFalse(999L);
    }

    @Test
    void givenNonExistingRecord_whenRemoveFavoriteRecordFromCustomer_thenThrowRecordNotFoundException() {
        Customer customer = customers.getFirst();

        when(customerRepository.findByIdAndIsDeletedFalse(customer.getId())).thenReturn(Optional.of(customer));
        when(vinylRecordRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> customerFavoritesService.removeFavoriteRecordFromCustomer(customer.getId(), 999L));
        verify(customerRepository).findByIdAndIsDeletedFalse(customer.getId());
        verify(vinylRecordRepository).findById(999L);
    }

}
