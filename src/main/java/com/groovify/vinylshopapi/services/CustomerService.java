package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CustomerMapper;
import com.groovify.vinylshopapi.mappers.VinylRecordMapper;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import com.groovify.vinylshopapi.specifications.CustomerSpecification;
import com.groovify.vinylshopapi.utils.SortHelper;
import com.groovify.vinylshopapi.validation.ValidationUtils;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RoleRepository roleRepository;
    private final ValidationUtils validationUtils;
    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordMapper vinylRecordMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, RoleRepository roleRepository,
                           ValidationUtils validationUtils, VinylRecordRepository vinylRecordRepository, VinylRecordMapper vinylRecordMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.roleRepository = roleRepository;
        this.validationUtils = validationUtils;
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordMapper = vinylRecordMapper;
    }

    public List<UserSummaryResponseDTO> getCustomers(
            String firstName,
            String lastName,
            Boolean newsletterSubscribed,
            String country,
            String city,
            String postalCode,
            String houseNumber,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = SortHelper.getSort(sortBy, sortOrder, List.of("id", "lastName", "email"));
        Specification<Customer> specification = CustomerSpecification.filterCustomers(
                firstName, lastName, newsletterSubscribed, country, city, postalCode, houseNumber
        );
        List<Customer> customers = customerRepository.findAll(specification, sort);

        return customerMapper.toUserSummaryResponseDTOs(customers);
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + id + " not found."));

        return customerMapper.toResponseDTO(customer);
    }

    public CustomerResponseDTO getCustomerByUsername(String username) {
        Customer customer = customerRepository.findByUsernameIgnoreCaseAndIsDeletedFalse(username)
                .orElseThrow(() -> new RecordNotFoundException("Customer with username " + username + " not found."));

        return customerMapper.toResponseDTO(customer);
    }

    public CustomerResponseDTO registerCustomer(CustomerRegisterDTO customerRegisterDTO) {
        Customer customer = customerMapper.toEntity(customerRegisterDTO);

        validationUtils.validateUniqueUsername(customerRegisterDTO.getUsername(), customer.getId());
        validationUtils.validateUniqueEmail(customerRegisterDTO.getEmail(), customer.getId());

        Role userRole = roleRepository.findByRoleType(RoleType.USER)
                .orElseThrow(() -> new RecordNotFoundException("Role '" + RoleType.USER + "' not found."));

        customer.getRoles().add(userRole);

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDTO(savedCustomer);
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerPatchDTO customerPatchDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + id + " not found."));

        if (customerPatchDTO.getUsername() != null) {
            validationUtils.validateUniqueUsername(customerPatchDTO.getUsername(), customer.getId());
        }

        if (customerPatchDTO.getEmail() != null) {
            validationUtils.validateUniqueEmail(customerPatchDTO.getEmail(), customer.getId());
        }

        customerMapper.partialUpdateCustomer(customerPatchDTO, customer);

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDTO(savedCustomer);
    }


    public List<VinylRecordSummaryResponseDTO> getFavoriteRecords(Long customerId) {
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + customerId + " not found."));

        List<VinylRecord> favoriteRecords = customer.getFavoriteVinylRecords();
        return vinylRecordMapper.toSummaryResponseDTOs(favoriteRecords);
    }

    public void addFavoriteRecordToCustomer(Long customerId, Long vinylRecordId) {
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + customerId + " not found."));

        VinylRecord favoriteRecord = vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + vinylRecordId + " not found."));

        if (customer.getFavoriteVinylRecords().contains(favoriteRecord)) {
            throw new ConflictException("Vinyl record with id " + vinylRecordId + " is already a favorite record of this customer.");
        }

        customer.getFavoriteVinylRecords().add(favoriteRecord);
        customerRepository.save(customer);
    }

    public void removeFavoriteRecordFromCustomer(Long customerId, Long vinylRecordId) {
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + customerId + " not found."));

        VinylRecord favoriteRecord = vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("Vinyl record with id " + vinylRecordId + " not found."));

        if (!customer.getFavoriteVinylRecords().contains(favoriteRecord)) {
            throw new ConflictException("Vinyl record with id " + vinylRecordId + " is not a favorite record of this customer.");
        }

        customer.getFavoriteVinylRecords().remove(favoriteRecord);
        customerRepository.save(customer);
    }
}
