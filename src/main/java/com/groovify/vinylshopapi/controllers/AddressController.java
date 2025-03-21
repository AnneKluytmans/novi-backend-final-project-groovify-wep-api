package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.AddressRequestDTO;
import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
import com.groovify.vinylshopapi.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping()
    public ResponseEntity<?> createOrderAddress(
            @Valid @RequestBody AddressRequestDTO addressRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        AddressResponseDTO newAddress = addressService.createOrderAddress(addressRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newAddress.getId())
                .toUri();
        return ResponseEntity.created(location).body(newAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderAddress(
            @PathVariable Long id
    ) {
        addressService.deleteOrderAddress(id);
        return ResponseEntity.noContent().build();
    }

}
