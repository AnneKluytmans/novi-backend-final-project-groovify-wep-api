package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.exceptions.BadRequestException;
import com.groovify.vinylshopapi.exceptions.ForbiddenException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
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

    public List<CustomerAddressResponseDTO> getCustomerAddresses(Long customerId) {
        if (!customerRepository.existsByIdAndIsDeletedFalse(customerId)) {
            throw new RecordNotFoundException("Customer with id " + customerId + " not found");
        }

        List<Address> customerAddresses = addressRepository.findAllByCustomerId(customerId);
        return addressMapper.toCustomerAddressResponseDTOs(customerAddresses);
    }

    public CustomerAddressResponseDTO getCustomerAddressById(Long customerId, Long addressId) {
        Address address = validateCustomerAndAddress(customerId, addressId);
        return addressMapper.toCustomerAddressResponseDTO(address);
    }

    public DefaultAddressesResponseDTO getDefaultCustomerAddresses(Long customerId) {
        if (!customerRepository.existsByIdAndIsDeletedFalse(customerId)) {
            throw new RecordNotFoundException("Customer with id " + customerId + " not found");
        }

        Address shippingAddress = findShippingAddress(addressRepository.findAllByCustomerId(customerId));
        Address billingAddress = findBillingAddress(addressRepository.findAllByCustomerId(customerId));

        DefaultAddressesResponseDTO defaultAddressesResponseDTO = new DefaultAddressesResponseDTO();
        defaultAddressesResponseDTO.setShippingAddress(addressMapper.toResponseDTO(shippingAddress));
        defaultAddressesResponseDTO.setBillingAddress(addressMapper.toResponseDTO(billingAddress));

        return defaultAddressesResponseDTO;
    }

    public AddressResponseDTO createCustomerAddress(Long customerId, AddressRequestDTO addressRequestDTO) {
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + customerId + " not found"));

        if (addressRequestDTO.getIsBillingAddress() == null || addressRequestDTO.getIsShippingAddress() == null) {
            throw new IllegalArgumentException("Is billing and shipping status is required.");
        }

        Address newAddress = addressMapper.toEntity(addressRequestDTO);

        List<Address> existingAddresses = customer.getAddresses();

        if (existingAddresses.size() >= 6) {
            throw new BadRequestException("Maximum number of addresses reached. Please remove at least one address before creating a new one.");
        }

        if (existingAddresses.isEmpty()) {
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

    public AddressResponseDTO updateCustomerAddress(Long customerId, Long addressId, AddressUpdateDTO addressUpdateDTO) {
        Address address = validateCustomerAndAddress(customerId, addressId);

        address.setStreet(addressUpdateDTO.getStreet());
        address.setHouseNumber(addressUpdateDTO.getHouseNumber());
        address.setCity(addressUpdateDTO.getCity());
        address.setPostalCode(addressUpdateDTO.getPostalCode());
        address.setCountry(addressUpdateDTO.getCountry());

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toResponseDTO(savedAddress);
    }

    public void setDefaultAddresses(Long customerId, Long addressId, DefaultAddressesRequestDTO defaultAddressesRequestDTO) {
        Address address = validateCustomerAndAddress(customerId, addressId);

        if (defaultAddressesRequestDTO.getIsBillingAddress()) {
            Address currentBillingAddress = findBillingAddress(address.getCustomer().getAddresses());
            if (currentBillingAddress == null || !currentBillingAddress.getId().equals(addressId)) {
                if (currentBillingAddress != null) {
                    currentBillingAddress.setIsBillingAddress(false);
                    addressRepository.save(currentBillingAddress);
                }
                address.setIsBillingAddress(true);
            }
        }

        if (defaultAddressesRequestDTO.getIsShippingAddress()) {
            Address currentShippingAddress = findShippingAddress(address.getCustomer().getAddresses());
            if (currentShippingAddress == null || !currentShippingAddress.getId().equals(addressId)) {
                if (currentShippingAddress != null) {
                    currentShippingAddress.setIsShippingAddress(false);
                    addressRepository.save(currentShippingAddress);
                }
                address.setIsShippingAddress(true);
            }
        }

        addressRepository.save(address);
    }

    public void deleteCustomerAddress(Long customerId, Long addressId) {
        Address address = validateCustomerAndAddress(customerId, addressId);

        List<Address> customerAddresses = address.getCustomer().getAddresses();

        if (address.getIsBillingAddress() || address.getIsShippingAddress()) {
            Address newDefaultAddress = findNewDefaultAddress(customerAddresses, addressId);
            if (newDefaultAddress != null) {
                if (address.getIsBillingAddress()) {
                    newDefaultAddress.setIsBillingAddress(true);
                }
                if (address.getIsShippingAddress()) {
                    newDefaultAddress.setIsShippingAddress(true);
                }
                addressRepository.save(newDefaultAddress);
            }
        }

        addressRepository.delete(address);
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

    private Address findNewDefaultAddress(List<Address> customerAddresses, Long excludedAddressId) {
        return customerAddresses.stream()
                .filter(address -> !address.getId().equals(excludedAddressId))
                .findFirst()
                .orElse(null);
    }

    private Address validateCustomerAndAddress(Long customerId, Long addressId) {
        if (!customerRepository.existsByIdAndIsDeletedFalse(customerId)) {
            throw new RecordNotFoundException("Customer with id " + customerId + " not found");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RecordNotFoundException("Address with id " + addressId + " not found"));

        if (address.getCustomer() == null || !address.getCustomer().getId().equals(customerId)) {
            throw new ForbiddenException("You cannot update or retrieve this address as it doesn't belong to the specified customer");
        }

        return address;
    }
}
