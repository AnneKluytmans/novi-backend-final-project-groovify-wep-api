package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.services.EmployeeAddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/employees/{id}/address")
public class EmployeeAddressController {

    private final EmployeeAddressService employeeAddressService;

    public EmployeeAddressController(EmployeeAddressService employeeAddressService) {
        this.employeeAddressService = employeeAddressService;
    }

    @GetMapping()
    public ResponseEntity<AddressResponseDTO> getEmployeeAddress(
            @PathVariable("id") Long employeeId
    ) {
        AddressResponseDTO address = employeeAddressService.getEmployeeAddress(employeeId);
        return ResponseEntity.ok(address);
    }

    @PostMapping()
    public ResponseEntity<?> createEmployeeAddress(
            @PathVariable("id") Long employeeId,
            @Valid @RequestBody AddressRequestDTO addressRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        AddressResponseDTO newAddress = employeeAddressService.createEmployeeAddress(employeeId, addressRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();

        return ResponseEntity.created(location).body(newAddress);
    }

    @PutMapping()
    public ResponseEntity<?> updateEmployeeAddress(
            @PathVariable("id") Long employeeId,
            @Valid @RequestBody AddressRequestDTO addressRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        AddressResponseDTO updatedAddress = employeeAddressService.updateEmployeeAddress(employeeId, addressRequestDTO);
        return ResponseEntity.ok(updatedAddress);
    }

}
