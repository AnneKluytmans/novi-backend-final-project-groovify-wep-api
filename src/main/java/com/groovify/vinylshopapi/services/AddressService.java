package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.AddressRequestDTO;
import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
import com.groovify.vinylshopapi.exceptions.DeleteOperationException;
import com.groovify.vinylshopapi.exceptions.ForbiddenException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.AddressMapper;
import com.groovify.vinylshopapi.models.Address;
import com.groovify.vinylshopapi.repositories.AddressRepository;
import com.groovify.vinylshopapi.specifications.AddressSpecification;
import com.groovify.vinylshopapi.utils.SortHelper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressService(
            AddressRepository addressRepository,
            AddressMapper addressMapper
    ) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public List<AddressResponseDTO> getAddresses(
            Long addressId,
            Long customerId,
            Long employeeId,
            String userType,
            Boolean inactiveUsers,
            Boolean isShipping,
            Boolean isBilling,
            Boolean isStandAlone,
            String country,
            String city,
            String postalCode,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = getAddressesSort(sortBy, sortOrder);
        Specification<Address> specification = AddressSpecification.filterAddresses(
                addressId, customerId, employeeId, userType, inactiveUsers, isShipping, isBilling,
                isStandAlone, country, city, postalCode
        );
        return addressMapper.toResponseDTOs(addressRepository.findAll(specification, sort));
    }

    public List<AddressResponseDTO> getCustomerShippingAddresses(
            String country,
            String city,
            String postalCode,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = getAddressesSort(sortBy, sortOrder);
        Specification<Address> specification = AddressSpecification.filterAddresses(
                null, null, null, "customer", false,
                true, null, null, country, city, postalCode
        );
        List<Address> addresses = addressRepository.findAll(specification, sort);
        return addressMapper.toResponseDTOs(addresses);
    }

    public AddressResponseDTO getAddressById(Long id) {
        Address address = findAddress(id);

        if (address.getCustomer() != null || address.getEmployee() != null) {
            throw new ForbiddenException("Address with id: " + id + " belongs to a customer or employee");
        }

        return addressMapper.toResponseDTO(address);
    }

    public AddressResponseDTO createStandAloneAddress(AddressRequestDTO addressRequestDTO) {
        Address address = addressMapper.toEntity(addressRequestDTO);
        return addressMapper.toResponseDTO(addressRepository.save(address));
    }

    public void deleteStandAloneAddress(Long id) {
        Address address = findAddress(id);
        validateIsStandAloneAddress(address);
        addressRepository.delete(address);
    }

    private Sort getAddressesSort(String sortBy, String sortOrder) {
        return SortHelper.getSort(sortBy, sortOrder, List.of("id", "country", "city", "postalCode"));
    }

    private Address findAddress(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new RecordNotFoundException("Address with id " + addressId + " not found"));
    }

    private void validateIsStandAloneAddress(Address address) {
        if (!address.isStandAlone()) {
            throw new DeleteOperationException("This address cannot be deleted as it is still in use by a user and/or order.");
        }
    }
}
