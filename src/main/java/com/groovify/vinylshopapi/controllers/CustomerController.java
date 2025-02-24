package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.CustomerPatchDTO;
import com.groovify.vinylshopapi.dtos.CustomerRegisterDTO;
import com.groovify.vinylshopapi.dtos.CustomerResponseDTO;
import com.groovify.vinylshopapi.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController()
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerRegisterDTO customerRegisterDTO,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        CustomerResponseDTO newCustomer = customerService.registerCustomer(customerRegisterDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCustomer.getId())
                .toUri();

        return ResponseEntity.created(location).body(newCustomer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id,
                                            @Valid @RequestBody CustomerPatchDTO customerPatchDTO,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(id, customerPatchDTO);
        return ResponseEntity.ok(updatedCustomer);
    }
}
