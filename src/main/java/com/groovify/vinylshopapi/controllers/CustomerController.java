package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController()
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public ResponseEntity<List<UserSummaryResponseDTO>> getCustomers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Boolean newsLetterSubscribed,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) String houseNumber,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder
    ) {
        List<UserSummaryResponseDTO> customers = customerService.getCustomers(
                firstName, lastName, newsLetterSubscribed, country, city,
                postalCode, houseNumber, sortBy, sortOrder
        );
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<CustomerResponseDTO> getCustomerByUsername(@PathVariable String username) {
        CustomerResponseDTO customer = customerService.getCustomerByUsername(username);
        return ResponseEntity.ok(customer);
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


    @GetMapping("/{customerId}/favorite-records")
    public ResponseEntity<List<VinylRecordSummaryResponseDTO>> getFavoriteRecords(@PathVariable Long customerId) {
        List<VinylRecordSummaryResponseDTO> favoriteRecords = customerService.getFavoriteRecords(customerId);
        return ResponseEntity.ok(favoriteRecords);
    }

    @PostMapping("/{customerId}/favorite-records/{recordId}")
    public ResponseEntity<Void> addFavoriteRecordToCustomer(@PathVariable Long customerId,
                                                            @PathVariable Long recordId) {
        customerService.addFavoriteRecordToCustomer(customerId, recordId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{customerId}/favorite-records/{recordId}")
    public ResponseEntity<Void> removeFavoriteRecordFromCustomer(@PathVariable Long customerId,
                                                                 @PathVariable Long recordId) {
        customerService.removeFavoriteRecordFromCustomer(customerId, recordId);
        return ResponseEntity.noContent().build();
    }
}
