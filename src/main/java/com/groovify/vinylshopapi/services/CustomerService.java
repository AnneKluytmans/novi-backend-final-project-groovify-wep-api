package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.CustomerPatchDTO;
import com.groovify.vinylshopapi.dtos.CustomerRegisterDTO;
import com.groovify.vinylshopapi.dtos.CustomerResponseDTO;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CustomerMapper;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.models.User;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, UserRepository userRepository, RoleRepository roleRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public CustomerResponseDTO registerCustomer(CustomerRegisterDTO customerRegisterDTO) {
        Customer customer = customerMapper.toEntity(customerRegisterDTO);

        validateUniqueUsername(customerRegisterDTO.getUsername(), customer.getId());
        validateUniqueEmail(customerRegisterDTO.getEmail(), customer.getId());

        Role userRole = roleRepository.findByRoleType(RoleType.USER)
                .orElseThrow(() -> new RecordNotFoundException("Role '" + RoleType.USER + "' not found."));

        customer.getRoles().add(userRole);

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDTO(savedCustomer);
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerPatchDTO customerPatchDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + id + " not found."));

        if (customerPatchDTO.getUsername() != null) {
            validateUniqueUsername(customerPatchDTO.getUsername(), customer.getId());
            customer.setUsername(customerPatchDTO.getUsername());
        }

        if (customerPatchDTO.getEmail() != null) {
            validateUniqueEmail(customerPatchDTO.getEmail(), customer.getId());
            customer.setEmail(customerPatchDTO.getEmail());
        }

        if (customerPatchDTO.getFirstName() != null && !customerPatchDTO.getFirstName().trim().isEmpty()) {
            customer.setFirstName(customerPatchDTO.getFirstName());
        }

        if (customerPatchDTO.getLastName() != null && !customerPatchDTO.getLastName().trim().isEmpty()) {
            customer.setLastName(customerPatchDTO.getLastName());
        }

        if (customerPatchDTO.getDateOfBirth() != null) {
            customer.setDateOfBirth(customerPatchDTO.getDateOfBirth());
        }

        if (customerPatchDTO.getPhone() != null) {
            customer.setPhone(customerPatchDTO.getPhone());
        }

        if (customerPatchDTO.getNewsletterSubscribed() != null) {
            customer.setNewsletterSubscribed(customerPatchDTO.getNewsletterSubscribed());
        }

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDTO(savedCustomer);
    }



    private void validateUniqueUsername(String username, Long currentUserId) {
        Optional<User> existingUser = userRepository.findByUsername(username.toLowerCase());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(currentUserId)) {
            throw new ConflictException("User with username " + username + " already exists.");
        }
    }

    private void validateUniqueEmail(String email, Long currentUserId) {
        Optional<User> existingUser = userRepository.findByEmail(email.toLowerCase());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(currentUserId)) {
            throw new ConflictException("User with email " + email + " already exists.");
        }
    }

}
