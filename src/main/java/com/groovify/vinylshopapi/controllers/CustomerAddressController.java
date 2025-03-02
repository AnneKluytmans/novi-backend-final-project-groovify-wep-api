package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.AddressRequestDTO;
import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
import com.groovify.vinylshopapi.dtos.AddressUpdateDTO;
import com.groovify.vinylshopapi.dtos.DefaultAddressesRequestDTO;
import com.groovify.vinylshopapi.services.CustomerAddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;

    public CustomerAddressController(CustomerAddressService customerAddressService) {
        this.customerAddressService = customerAddressService;
    }

    @PostMapping()
    public ResponseEntity<?> createCustomerAddress(@PathVariable("customerId") Long customerId,
                                                   @Valid @RequestBody AddressRequestDTO addressRequestDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        AddressResponseDTO newAddress = customerAddressService.createCustomerAddress(customerId, addressRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newAddress.getId())
                .toUri();
        return ResponseEntity.created(location).body(newAddress);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateCustomerAddress(@PathVariable("customerId") Long customerId,
                                                   @PathVariable("addressId") Long addressId,
                                                   @Valid @RequestBody AddressUpdateDTO addressUpdateDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        AddressResponseDTO updatedAddress = customerAddressService.updateCustomerAddress(customerId, addressId, addressUpdateDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @PatchMapping("/{addressId}/defaults")
    public ResponseEntity<?> setDefaultAddresses(@PathVariable("customerId") Long customerId,
                                                 @PathVariable("addressId") Long addressId,
                                                 @Valid @RequestBody DefaultAddressesRequestDTO defaultAddressesRequestDTO,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        customerAddressService.setDefaultAddresses(customerId, addressId, defaultAddressesRequestDTO);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteCustomerAddress(@PathVariable("customerId") Long customerId,
                                                      @PathVariable ("addressId") Long addressId) {
        customerAddressService.deleteCustomerAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }

}
