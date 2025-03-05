package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
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

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
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
            String country,
            String city,
            String postalCode,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = getAddressesSort(sortBy, sortOrder);

        Specification<Address> specification = AddressSpecification.filterAddresses(
                addressId, customerId, employeeId, userType, inactiveUsers, isShipping, isBilling,
                country, city, postalCode
        );
        List<Address> addresses = addressRepository.findAll(specification, sort);

        return addressMapper.toResponseDTOs(addresses);
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
                true, null, country, city, postalCode
        );
        List<Address> addresses = addressRepository.findAll(specification, sort);
        return addressMapper.toResponseDTOs(addresses);
    }

    private Sort getAddressesSort(String sortBy, String sortOrder) {
        return SortHelper.getSort(sortBy, sortOrder, List.of("id", "country", "city", "postalCode"));
    }
}
