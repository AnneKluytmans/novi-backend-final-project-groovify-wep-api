package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.AddressRequestDTO;
import com.groovify.vinylshopapi.dtos.AddressSummaryResponseDTO;
import com.groovify.vinylshopapi.exceptions.ConflictException;
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

    public AddressSummaryResponseDTO getEmployeeAddressById(Long employeeId) {
        Address address = validateEmployeeAndAddress(employeeId);
        return addressMapper.toSummaryResponseDTO(address);
    }

    public AddressSummaryResponseDTO createEmployeeAddress(Long employeeId, AddressRequestDTO addressRequestDTO) {

        if (addressRepository.existsByEmployeeId(employeeId)) {
            throw new ConflictException("Employee already has an address");
        }

        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(employeeId)
                .orElseThrow(() -> new RecordNotFoundException("Employee with id " + employeeId + " not found"));

        Address address = addressMapper.toEntity(addressRequestDTO);

        address.setEmployee(employee);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toSummaryResponseDTO(savedAddress);
    }

    public AddressSummaryResponseDTO updateEmployeeAddress(Long employeeId, AddressRequestDTO addressRequestDTO) {
        Address address = validateEmployeeAndAddress(employeeId);

        address.setStreet(addressRequestDTO.getStreet());
        address.setHouseNumber(addressRequestDTO.getHouseNumber());
        address.setCity(addressRequestDTO.getCity());
        address.setPostalCode(addressRequestDTO.getPostalCode());
        address.setCountry(addressRequestDTO.getCountry());

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toSummaryResponseDTO(savedAddress);
    }

    private Address validateEmployeeAndAddress(Long employeeId) {
        if (!employeeRepository.existsByIdAndIsDeletedFalse(employeeId)) {
            throw new RecordNotFoundException("Employee with id " + employeeId + " not found");
        }

        return addressRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RecordNotFoundException("Address of employee with id " + employeeId + " not found"));
    }
}
