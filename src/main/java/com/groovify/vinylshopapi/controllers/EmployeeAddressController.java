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
@RequestMapping("/api/employees/{employeeId}/address")
public class EmployeeAddressController {

    private final EmployeeAddressService employeeAddressService;

    public EmployeeAddressController(EmployeeAddressService employeeAddressService) {
        this.employeeAddressService = employeeAddressService;
    }

    @PostMapping()
    public ResponseEntity<?> createEmployeeAddress(
            @PathVariable("employeeId") Long employeeId,
            @Valid @RequestBody AddressRequestDTO addressRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        AddressSummaryResponseDTO newAddress = employeeAddressService.createEmployeeAddress(employeeId, addressRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();

        return ResponseEntity.created(location).body(newAddress);
    }

    @PutMapping()
    public ResponseEntity<?> updateEmployeeAddress(
            @PathVariable("employeeId") Long employeeId,
            @Valid @RequestBody AddressRequestDTO addressRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        AddressSummaryResponseDTO updatedAddress = employeeAddressService.updateEmployeeAddress(employeeId, addressRequestDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @GetMapping()
    public ResponseEntity<AddressSummaryResponseDTO> getEmployeeAddress(
            @PathVariable("employeeId") Long employeeId
    ) {
        AddressSummaryResponseDTO address = employeeAddressService.getEmployeeAddressById(employeeId);
        return ResponseEntity.ok(address);
    }

}
