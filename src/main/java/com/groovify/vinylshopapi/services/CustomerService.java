package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CustomerMapper;
import com.groovify.vinylshopapi.mappers.OrderMapper;
import com.groovify.vinylshopapi.mappers.VinylRecordMapper;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Order;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.models.VinylRecord;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.OrderRepository;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import com.groovify.vinylshopapi.specifications.CustomerSpecification;
import com.groovify.vinylshopapi.utils.SortHelper;
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
    private final VinylRecordRepository vinylRecordRepository;
    private final VinylRecordMapper vinylRecordMapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public CustomerService(
            CustomerRepository customerRepository,
            CustomerMapper customerMapper,
            RoleRepository roleRepository,
            ValidationUtils validationUtils,
            VinylRecordRepository vinylRecordRepository,
            VinylRecordMapper vinylRecordMapper,
            OrderRepository orderRepository,
            OrderMapper orderMapper
    ) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.roleRepository = roleRepository;
        this.validationUtils = validationUtils;
        this.vinylRecordRepository = vinylRecordRepository;
        this.vinylRecordMapper = vinylRecordMapper;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
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
        List<Customer> customers = customerRepository.findAll(specification, sort);

        return customerMapper.toUserSummaryResponseDTOs(customers);
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        return customerMapper.toResponseDTO(findCustomer(id));
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

    public CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = findCustomer(id);

        validationUtils.validateUniqueUsername(customerUpdateDTO.getUsername(), id);
        validationUtils.validateUniqueEmail(customerUpdateDTO.getEmail(), id);

        customerMapper.updateCustomer(customerUpdateDTO, customer);

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDTO(savedCustomer);
    }


    public List<VinylRecordSummaryResponseDTO> getFavoriteRecords(Long customerId) {
        List<VinylRecord> favoriteRecords = findCustomer(customerId).getFavoriteVinylRecords();
        return vinylRecordMapper.toSummaryResponseDTOs(favoriteRecords);
    }

    public void addFavoriteRecordToCustomer(Long customerId, Long vinylRecordId) {
        Customer customer = findCustomer(customerId);
        VinylRecord favoriteRecord = findVinylRecord(vinylRecordId);

        if (customer.getFavoriteVinylRecords().contains(favoriteRecord)) {
            throw new ConflictException("Vinyl record with id " + vinylRecordId + " is already a favorite record of this customer.");
        }

        customer.getFavoriteVinylRecords().add(favoriteRecord);
        customerRepository.save(customer);
    }

    public void removeFavoriteRecordFromCustomer(Long customerId, Long vinylRecordId) {
        Customer customer = findCustomer(customerId);
        VinylRecord favoriteRecord = findVinylRecord(vinylRecordId);

        if (!customer.getFavoriteVinylRecords().contains(favoriteRecord)) {
            throw new ConflictException("Vinyl record with id " + vinylRecordId + " is not a favorite record of this customer.");
        }

        customer.getFavoriteVinylRecords().remove(favoriteRecord);
        customerRepository.save(customer);
    }

    public List<OrderSummaryResponseDTO> getOrdersByCustomer(Long customerId) {
        Customer customer = findCustomer(customerId);
        return orderMapper.toOrderSummaryResponseDTOs(customer.getOrders());
    }

    public OrderResponseDTO getOrderByCustomerAndId(Long customerId, Long orderId) {
        return orderMapper.toResponseDTO(findOrder(orderId, customerId));
    }

    private Customer findCustomer(Long customerId) {
        return customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("No customer found with id: " + customerId));
    }

    private VinylRecord findVinylRecord(Long vinylRecordId) {
        return vinylRecordRepository.findById(vinylRecordId)
                .orElseThrow(() -> new RecordNotFoundException("No vinyl record found with id: " + vinylRecordId));
    }

    private Order findOrder(Long orderId, Long customerId) {
        return orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new RecordNotFoundException("No order found with id: " + orderId + " for customer with id: " + customerId));
    }
}
