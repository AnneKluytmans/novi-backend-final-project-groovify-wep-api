package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.AddressRequestDTO;
import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
import com.groovify.vinylshopapi.dtos.AddressUpdateDTO;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.ForbiddenException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.AddressMapper;
import com.groovify.vinylshopapi.models.Address;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.repositories.AddressRepository;
import com.groovify.vinylshopapi.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeAddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final EmployeeRepository employeeRepository;

    private EmployeeAddressService(AddressRepository addressRepository, AddressMapper addressMapper,
                                   EmployeeRepository employeeRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.employeeRepository = employeeRepository;
    }

    public AddressResponseDTO getEmployeeAddressById(Long employeeId) {
        Address address = validateEmployeeAndAddress(employeeId);
        return addressMapper.toResponseDTO(address);
    }

    public AddressResponseDTO createEmployeeAddress(Long employeeId, AddressRequestDTO addressRequestDTO) {

        if (addressRequestDTO.getIsBillingAddress() != null || addressRequestDTO.getIsShippingAddress() != null) {
            throw new IllegalArgumentException("Employee address cannot be a billing or shipping address. Is billing and shipping status must be null");
        }

        if (addressRepository.existsByEmployeeId(employeeId)) {
            throw new ConflictException("Employee already has an address");
        }

        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(employeeId)
                .orElseThrow(() -> new RecordNotFoundException("Employee with id " + employeeId + " not found"));

        Address address = addressMapper.toEntity(addressRequestDTO);

        address.setEmployee(employee);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toResponseDTO(savedAddress);
    }

    public AddressResponseDTO updateEmployeeAddress(Long employeeId, AddressUpdateDTO addressUpdateDTO) {
        Address address = validateEmployeeAndAddress(employeeId);

        address.setStreet(addressUpdateDTO.getStreet());
        address.setHouseNumber(addressUpdateDTO.getHouseNumber());
        address.setCity(addressUpdateDTO.getCity());
        address.setPostalCode(addressUpdateDTO.getPostalCode());
        address.setCountry(addressUpdateDTO.getCountry());

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toResponseDTO(savedAddress);
    }

    private Address validateEmployeeAndAddress(Long employeeId) {
        if (!employeeRepository.existsByIdAndIsDeletedFalse(employeeId)) {
            throw new RecordNotFoundException("Employee with id " + employeeId + " not found");
        }

        return addressRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RecordNotFoundException("Address of employee with id " + employeeId + " not found"));
    }
}
