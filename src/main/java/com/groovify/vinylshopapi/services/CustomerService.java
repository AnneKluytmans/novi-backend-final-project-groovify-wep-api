package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.CustomerPatchDTO;
import com.groovify.vinylshopapi.dtos.CustomerRegisterDTO;
import com.groovify.vinylshopapi.dtos.CustomerResponseDTO;
import com.groovify.vinylshopapi.dtos.UserSummaryResponseDTO;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.enums.SortOrder;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CustomerMapper;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.specifications.CustomerSpecification;
import com.groovify.vinylshopapi.validation.ValidationUtils;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RoleRepository roleRepository;
    private final ValidationUtils validationUtils;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper,
                           RoleRepository roleRepository, ValidationUtils validationUtils) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.roleRepository = roleRepository;
        this.validationUtils = validationUtils;
    }

    public List<UserSummaryResponseDTO> getCustomers(String firstName, String lastName, Boolean newsletterSubscribed,
                                                     String sortBy, String sortOrder) {
        Sort sort = switch (sortBy.trim().toLowerCase()) {
            case "id" -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("id") : Sort.Order.asc("id"));
            case "email" -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("email") : Sort.Order.asc("email"));
            default -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("lastName") : Sort.Order.asc("lastName"));
        };

        Specification<Customer> specification = CustomerSpecification.filterCustomers(firstName, lastName, newsletterSubscribed);
        List<Customer> customers = customerRepository.findAll(specification, sort);

        return customerMapper.toUserSummaryResponseDTOs(customers);
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + id + " not found."));

        return customerMapper.toResponseDTO(customer);
    }

    public CustomerResponseDTO getCustomerByUsername(String username) {
        Customer customer = customerRepository.findByUsernameIgnoreCaseAndIsDeletedFalse(username)
                .orElseThrow(() -> new RecordNotFoundException("Customer with username " + username + " not found."));

        return customerMapper.toResponseDTO(customer);
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
