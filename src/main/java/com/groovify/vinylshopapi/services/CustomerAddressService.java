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

    public CustomerAddressService(
            AddressRepository addressRepository,
            AddressMapper addressMapper,
            CustomerRepository customerRepository
    ) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.customerRepository = customerRepository;
    }

    public List<CustomerAddressResponseDTO> getCustomerAddresses(Long customerId) {
        Customer customer = findCustomer(customerId);
        return addressMapper.toCustomerResponseDTOs(customer.getAddresses());
    }

    public CustomerAddressResponseDTO getCustomerAddressById(Long customerId, Long addressId) {
        Address address = validateCustomerAndAddress(customerId, addressId);
        return addressMapper.toCustomerResponseDTO(address);
    }

    public DefaultAddressesResponseDTO getDefaultCustomerAddresses(Long customerId) {
        Customer customer = findCustomer(customerId);
        List<Address> customerAddresses = customer.getAddresses();

        return new DefaultAddressesResponseDTO(
                addressMapper.toResponseDTO(findShippingAddress(customerAddresses)),
                addressMapper.toResponseDTO(findBillingAddress(customerAddresses))
        );
    }

    public CustomerAddressResponseDTO createCustomerAddress(Long customerId, CustomerAddressRequestDTO addressRequestDTO) {
        Customer customer = findCustomer(customerId);
        List<Address> existingAddresses = customer.getAddresses();

        if (existingAddresses.size() >= 6) {
            throw new BadRequestException("Maximum number of addresses reached. Please remove at least one address before creating a new one.");
        }

        Address newAddress = addressMapper.toCustomerAddressEntity(addressRequestDTO);
        newAddress.setCustomer(customer);

        if (existingAddresses.isEmpty()) {
            newAddress.setIsBillingAddress(true);
            newAddress.setIsShippingAddress(true);
        } else {
            DefaultAddressesRequestDTO defaultAddressesRequestDTO = new DefaultAddressesRequestDTO(
                    addressRequestDTO.getIsShippingAddress(),
                    addressRequestDTO.getIsBillingAddress()
            );
            updateDefaultAddress(existingAddresses, defaultAddressesRequestDTO, newAddress);
        }

        Address savedAddress = addressRepository.save(newAddress);
        return addressMapper.toCustomerResponseDTO(savedAddress);
    }

    public CustomerAddressResponseDTO updateCustomerAddress(Long customerId, Long addressId, AddressRequestDTO addressRequestDTO) {
        Address address = validateCustomerAndAddress(customerId, addressId);

        addressMapper.updateAddress(addressRequestDTO, address);

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toCustomerResponseDTO(savedAddress);
    }

    public void setDefaultAddresses(Long customerId, Long addressId, DefaultAddressesRequestDTO defaultAddressesRequestDTO) {
        Address address = validateCustomerAndAddress(customerId, addressId);

        updateDefaultAddress(address.getCustomer().getAddresses(), defaultAddressesRequestDTO, address);
        addressRepository.save(address);
    }

    public void deleteCustomerAddress(Long customerId, Long addressId) {
        Address address = validateCustomerAndAddress(customerId, addressId);
        List<Address> customerAddresses = address.getCustomer().getAddresses();

        if (address.getIsBillingAddress() || address.getIsShippingAddress()) {
            Address newDefaultAddress = findNewDefaultAddress(customerAddresses, addressId);
            if (newDefaultAddress != null) {
                newDefaultAddress.setIsBillingAddress(address.getIsBillingAddress());
                newDefaultAddress.setIsShippingAddress(address.getIsShippingAddress());
                addressRepository.save(newDefaultAddress);
            }
        }
        addressRepository.delete(address);
    }


    private Customer findCustomer(Long customerId) {
        return customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + customerId + " not found"));
    }

    private Address validateCustomerAndAddress(Long customerId, Long addressId) {
        Customer customer = findCustomer(customerId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RecordNotFoundException("Address with id " + addressId + " not found"));

        if (!address.getCustomer().equals(customer)) {
            throw new ForbiddenException("This address doesn't belong to the specified customer");
        }

        return address;
    }

    private void updateDefaultAddress(List<Address> addresses, DefaultAddressesRequestDTO addressRequestDTO, Address newDefaultAddress) {
        if (addressRequestDTO.getIsBillingAddress()) {
            resetCurrentBillingAddress(addresses);
            newDefaultAddress.setIsBillingAddress(true);
        }
        if (addressRequestDTO.getIsShippingAddress()) {
            resetCurrentShippingAddress(addresses);
            newDefaultAddress.setIsShippingAddress(true);
        }
    }

    private void resetCurrentBillingAddress(List<Address> addresses) {
        for (Address address : addresses) {
            if (address.getIsBillingAddress()) {
                address.setIsBillingAddress(false);
                addressRepository.save(address);
                break;
            }
        }
    }

    private void resetCurrentShippingAddress(List<Address> addresses) {
        for (Address address : addresses) {
            if (address.getIsShippingAddress()) {
                address.setIsShippingAddress(false);
                addressRepository.save(address);
                break;
            }
        }
    }

    private Address findBillingAddress(List<Address> addresses) {
        for (Address address : addresses) {
            if (address.getIsBillingAddress()) {
                return address;
            }
        }
        return null;
    }

    private Address findShippingAddress(List<Address> addresses) {
        for (Address address : addresses) {
            if (address.getIsShippingAddress()) {
                return address;
            }
        }
        return null;
    }

    private Address findNewDefaultAddress(List<Address> addresses, Long excludedAddressId) {
        for (Address address : addresses) {
            if (!address.getId().equals(excludedAddressId)) {
                return address;
            }
        }
        return null;
    }

}
