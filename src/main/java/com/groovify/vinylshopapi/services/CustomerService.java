package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CustomerMapper;
import com.groovify.vinylshopapi.mappers.OrderMapper;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Order;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.OrderRepository;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.specifications.CustomerSpecification;
import com.groovify.vinylshopapi.utils.SortHelper;
import com.groovify.vinylshopapi.validation.ValidationUtils;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RoleRepository roleRepository;
    private final ValidationUtils validationUtils;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(
            CustomerRepository customerRepository,
            CustomerMapper customerMapper,
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            RoleRepository roleRepository,
            ValidationUtils validationUtils,
            PasswordEncoder passwordEncoder
    ) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.roleRepository = roleRepository;
        this.validationUtils = validationUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserSummaryResponseDTO> getCustomers(
            String firstName,
            String lastName,
            Boolean newsletterSubscribed,
            String country,
            String city,
            String postalCode,
            String houseNumber,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = SortHelper.getSort(sortBy, sortOrder, List.of("id", "lastName", "email"));
        Specification<Customer> specification = CustomerSpecification.filterCustomers(
                firstName, lastName, newsletterSubscribed, country, city, postalCode, houseNumber
        );
        return customerMapper.toUserSummaryResponseDTOs(customerRepository.findAll(specification, sort));
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        return customerMapper.toResponseDTO(findCustomer(id));
    }

    public CustomerResponseDTO getCustomerByUsername(String username) {
        Customer customer = customerRepository.findByUsernameIgnoreCaseAndIsDeletedFalse(username)
                .orElseThrow(() -> new RecordNotFoundException("No customer found with username: " + username));

        return customerMapper.toResponseDTO(customer);
    }

    public CustomerResponseDTO registerCustomer(CustomerRegisterDTO customerRegisterDTO) {
        Customer customer = customerMapper.toEntity(customerRegisterDTO);

        validationUtils.validateUniqueUsername(customerRegisterDTO.getUsername(), customer.getId());
        validationUtils.validateUniqueEmail(customerRegisterDTO.getEmail(), customer.getId());

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        Role userRole = roleRepository.findByRoleType(RoleType.USER)
                .orElseThrow(() -> new RecordNotFoundException("Role '" + RoleType.USER + "' not found."));

        customer.getRoles().add(userRole);

        return customerMapper.toResponseDTO(customerRepository.save(customer));
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = findCustomer(id);

        validationUtils.validateUniqueUsername(customerUpdateDTO.getUsername(), id);
        validationUtils.validateUniqueEmail(customerUpdateDTO.getEmail(), id);

        customerMapper.updateCustomer(customerUpdateDTO, customer);
        return customerMapper.toResponseDTO(customerRepository.save(customer));
    }


    public List<OrderSummaryResponseDTO> getCustomerOrders(Long customerId) {
        return orderMapper.toOrderSummaryResponseDTOs(findCustomer(customerId).getOrders());
    }

    public OrderResponseDTO getCustomerOrder(Long customerId, Long orderId) {
        return orderMapper.toResponseDTO(findOrder(orderId, customerId));
    }


    private Customer findCustomer(Long customerId) {
        return customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("No customer found with id: " + customerId));
    }

    private Order findOrder(Long orderId, Long customerId) {
        return orderRepository.findByIdAndIsDeletedFalseAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new RecordNotFoundException("No order found with id: " + orderId + " for customer with id: " + customerId));
    }
}
