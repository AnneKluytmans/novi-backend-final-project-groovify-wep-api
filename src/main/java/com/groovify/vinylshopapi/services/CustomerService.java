package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.CustomerPatchDTO;
import com.groovify.vinylshopapi.dtos.CustomerRegisterDTO;
import com.groovify.vinylshopapi.dtos.CustomerResponseDTO;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CustomerMapper;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.repositories.UserRepository;
import com.groovify.vinylshopapi.validation.ValidationUtils;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ValidationUtils validationUtils;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, UserRepository userRepository,
                           RoleRepository roleRepository, ValidationUtils validationUtils) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.validationUtils = validationUtils;
    }

    public CustomerResponseDTO registerCustomer(CustomerRegisterDTO customerRegisterDTO) {
        Customer customer = customerMapper.toEntity(customerRegisterDTO);

        validationUtils.validateUniqueUsername(customerRegisterDTO.getUsername(), customer.getId());
        validationUtils.validateUniqueEmail(customerRegisterDTO.getEmail(), customer.getId());

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
            validationUtils.validateUniqueUsername(customerPatchDTO.getUsername(), customer.getId());
            customer.setUsername(customerPatchDTO.getUsername());
        }

        if (customerPatchDTO.getEmail() != null) {
            validationUtils.validateUniqueEmail(customerPatchDTO.getEmail(), customer.getId());
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

}
