package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.AddressRequestDTO;
import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
import com.groovify.vinylshopapi.mappers.AddressMapper;
import com.groovify.vinylshopapi.models.Address;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.repositories.AddressRepository;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerAddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerRepository customerRepository;

    public CustomerAddressService(AddressRepository addressRepository, AddressMapper addressMapper,
                                  CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.customerRepository = customerRepository;
    }

    public AddressResponseDTO createCustomerAddress(Long customerId, AddressRequestDTO addressRequestDTO) {
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RuntimeException("Customer with id " + customerId + " not found"));

        if (addressRequestDTO.getIsBillingAddress() == null || addressRequestDTO.getIsShippingAddress() == null) {
            throw new IllegalArgumentException("Is billing and shipping status is required.");
        }

        Address newAddress = addressMapper.toEntity(addressRequestDTO);

        List<Address> existingAddresses = customer.getAddresses();

        if (existingAddresses == null || existingAddresses.isEmpty()) {
            newAddress.setIsBillingAddress(true);
            newAddress.setIsShippingAddress(true);
        } else {
            if (addressRequestDTO.getIsBillingAddress()) {
                Address currentBillingAddress = findBillingAddress(existingAddresses);
                if (currentBillingAddress != null) {
                    currentBillingAddress.setIsBillingAddress(false);
                    addressRepository.save(currentBillingAddress);
                }
                newAddress.setIsBillingAddress(true);
            }

            if (addressRequestDTO.getIsShippingAddress()) {
                Address currentShippingAddress = findShippingAddress(existingAddresses);
                if (currentShippingAddress != null) {
                    currentShippingAddress.setIsShippingAddress(false);
                    addressRepository.save(currentShippingAddress);
                }
                newAddress.setIsShippingAddress(true);
            }
        }

        newAddress.setCustomer(customer);
        Address savedAddress = addressRepository.save(newAddress);
        return addressMapper.toResponseDTO(savedAddress);
    }

    private Address findBillingAddress(List<Address> addresses) {
        return addresses.stream()
                .filter(Address::getIsBillingAddress)
                .findFirst()
                .orElse(null);
    }

    private Address findShippingAddress(List<Address> addresses) {
        return addresses.stream()
                .filter(Address::getIsShippingAddress)
                .findFirst()
                .orElse(null);
    }
}
