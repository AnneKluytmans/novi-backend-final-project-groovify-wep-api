package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
import com.groovify.vinylshopapi.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping()
    public ResponseEntity<List<AddressResponseDTO>> getAddresses(
            @RequestParam(required = false) Long addressId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String userType,
            @RequestParam(defaultValue = "false") Boolean inactiveUsers,
            @RequestParam(required = false) Boolean isShipping,
            @RequestParam(required = false) Boolean isBilling,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String postalCode,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        List<AddressResponseDTO> addresses = addressService.getAddresses(
                addressId, customerId, employeeId, userType, inactiveUsers,
                isShipping, isBilling, country, city, postalCode, sortBy, sortOrder
        );
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<AddressResponseDTO>> getCustomerShippingAddresses(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String postalCode,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        List<AddressResponseDTO> addresses = addressService.getCustomerShippingAddresses(
                country, city, postalCode, sortBy, sortOrder
        );
        return ResponseEntity.ok(addresses);
    }

}
