package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.AddressRequestDTO;
import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
import com.groovify.vinylshopapi.dtos.AddressUpdateDTO;
import com.groovify.vinylshopapi.exceptions.ForbiddenException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.AddressMapper;
import com.groovify.vinylshopapi.models.Address;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.repositories.AddressRepository;
import com.groovify.vinylshopapi.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

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

    public AddressResponseDTO createEmployeeAddress(Long employeeId, AddressRequestDTO addressRequestDTO) {
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(employeeId)
                .orElseThrow(() -> new RecordNotFoundException("Employee with id " + employeeId + " not found"));

        if (addressRequestDTO.getIsBillingAddress() != null || addressRequestDTO.getIsShippingAddress() != null) {
            throw new IllegalArgumentException("Employee address cannot be a billing or shipping address. Is billing and shipping status must be null");
        }

        Address address = addressMapper.toEntity(addressRequestDTO);

        address.setEmployee(employee);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toResponseDTO(savedAddress);
    }

    public AddressResponseDTO updateEmployeeAddress(Long employeeId, Long addressId, AddressUpdateDTO addressUpdateDTO) {
        Address address = validateEmployeeAndAddress(employeeId, addressId);

        address.setStreet(addressUpdateDTO.getStreet());
        address.setHouseNumber(addressUpdateDTO.getHouseNumber());
        address.setCity(addressUpdateDTO.getCity());
        address.setPostalCode(addressUpdateDTO.getPostalCode());
        address.setCountry(addressUpdateDTO.getCountry());

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toResponseDTO(savedAddress);
    }

    private Address validateEmployeeAndAddress(Long employeeId, Long addressId) {
        if (!employeeRepository.existsByIdAndIsDeletedFalse(employeeId)) {
            throw new RecordNotFoundException("Employee with id " + employeeId + " not found");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RecordNotFoundException("Address with id " + addressId + " not found"));

        if (address.getEmployee() == null || !address.getEmployee().getId().equals(employeeId)) {
            throw new ForbiddenException("You cannot update or retrieve this address as it doesn't belong to the specified employee");
        }

        return address;
    }
}
