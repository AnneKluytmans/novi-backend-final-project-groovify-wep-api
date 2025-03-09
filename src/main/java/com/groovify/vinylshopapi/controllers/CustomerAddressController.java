package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.services.CustomerAddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers/{id}/addresses")
public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;

    public CustomerAddressController(CustomerAddressService customerAddressService) {
        this.customerAddressService = customerAddressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getCustomerAddresses(
            @PathVariable("id") Long customerId
    ) {
        List<AddressResponseDTO> addresses = customerAddressService.getCustomerAddresses(customerId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> getCustomerAddress(
            @PathVariable("id") Long customerId,
            @PathVariable("addressId") Long addressId
    ) {
        AddressResponseDTO address = customerAddressService.getCustomerAddressById(customerId, addressId);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/defaults")
    public ResponseEntity<DefaultAddressesResponseDTO> getDefaultCustomerAddresses(
            @PathVariable("id") Long customerId
    ) {
        DefaultAddressesResponseDTO defaultAddresses = customerAddressService.getDefaultCustomerAddresses(customerId);
        return ResponseEntity.ok(defaultAddresses);
    }

    @PostMapping()
    public ResponseEntity<?> createCustomerAddress(
            @PathVariable("id") Long customerId,
            @Valid @RequestBody CustomerAddressRequestDTO addressRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        AddressResponseDTO newAddress = customerAddressService.createCustomerAddress(customerId, addressRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{addressId}")
                .buildAndExpand(newAddress.getId())
                .toUri();
        return ResponseEntity.created(location).body(newAddress);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateCustomerAddress(
            @PathVariable("id") Long customerId,
            @PathVariable("addressId") Long addressId,
            @Valid @RequestBody AddressRequestDTO addressRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        AddressResponseDTO updatedAddress = customerAddressService.updateCustomerAddress(customerId, addressId, addressRequestDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @PatchMapping("/{addressId}/defaults")
    public ResponseEntity<?> setDefaultAddresses(
            @PathVariable("id") Long customerId,
            @PathVariable("addressId") Long addressId,
            @Valid @RequestBody DefaultAddressesRequestDTO defaultAddressesRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        customerAddressService.setDefaultAddresses(customerId, addressId, defaultAddressesRequestDTO);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteCustomerAddress(
            @PathVariable("id") Long customerId,
            @PathVariable ("addressId") Long addressId
    ) {
        customerAddressService.deleteCustomerAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }

}
