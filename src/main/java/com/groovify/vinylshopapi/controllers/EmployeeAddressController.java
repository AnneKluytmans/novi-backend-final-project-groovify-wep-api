package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.AddressRequestDTO;
import com.groovify.vinylshopapi.dtos.AddressResponseDTO;
import com.groovify.vinylshopapi.dtos.AddressUpdateDTO;
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
    public ResponseEntity<?> createEmployeeAddress(@PathVariable("employeeId") Long employeeId,
                                                   @Valid @RequestBody AddressRequestDTO addressRequestDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        AddressResponseDTO newAddress = employeeAddressService.createEmployeeAddress(employeeId, addressRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();

        return ResponseEntity.created(location).body(newAddress);
    }

    @PutMapping()
    public ResponseEntity<?> updateEmployeeAddress(@PathVariable("employeeId") Long employeeId,
                                                   @Valid @RequestBody AddressUpdateDTO addressUpdateDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        AddressResponseDTO updatedAddress = employeeAddressService.updateEmployeeAddress(employeeId, addressUpdateDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @GetMapping()
    public ResponseEntity<AddressResponseDTO> getEmployeeAddress(@PathVariable("employeeId") Long employeeId) {
        AddressResponseDTO address = employeeAddressService.getEmployeeAddressById(employeeId);
        return ResponseEntity.ok(address);
    }

}
