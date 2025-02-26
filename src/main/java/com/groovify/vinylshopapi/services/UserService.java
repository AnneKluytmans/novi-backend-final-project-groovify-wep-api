package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.ReactivateUserDTO;
import com.groovify.vinylshopapi.dtos.UserResponseDTO;
import com.groovify.vinylshopapi.dtos.UserSummaryResponseDTO;
import com.groovify.vinylshopapi.enums.SortOrder;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.InvalidVerificationException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CustomerMapper;
import com.groovify.vinylshopapi.mappers.EmployeeMapper;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.models.User;
import com.groovify.vinylshopapi.repositories.UserRepository;

import com.groovify.vinylshopapi.specifications.UserSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;
    private final EmployeeMapper employeeMapper;

    public UserService(UserRepository userRepository, CustomerMapper customerMapper, EmployeeMapper employeeMapper) {
        this.userRepository = userRepository;
        this.customerMapper = customerMapper;
        this.employeeMapper = employeeMapper;
    }


    public List<UserSummaryResponseDTO> getUsers(String userType, String firstName, String lastName, Boolean isDeleted, String deletedAfter,
                                                 String deletedBefore, String sortBy, String sortOrder) {
        Sort sort = switch (sortBy.toLowerCase()) {
            case "id" -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("id") : Sort.Order.asc("id"));
            case "email" -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("email") : Sort.Order.asc("email"));
            default -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("lastName") : Sort.Order.asc("lastName"));
        };

        Specification<User> specification = UserSpecification.filterUsers(userType, firstName, lastName, isDeleted, deletedAfter, deletedBefore);
        List<User> users = userRepository.findAll(specification, sort);

        List<UserSummaryResponseDTO> userSummaryResponseDTOS = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Customer) {
                userSummaryResponseDTOS.add(customerMapper.toUserSummaryResponseDTO((Customer) user));
            }
            if (user instanceof Employee) {
                userSummaryResponseDTOS.add(employeeMapper.toUserSummaryResponseDTO((Employee) user));
            }
        }

        return userSummaryResponseDTOS;
    }


    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        if (user instanceof Customer) {
            return customerMapper.toUserResponseDTO((Customer) user);
        }

        if (user instanceof Employee) {
           return employeeMapper.toUserResponseDTO((Employee) user);
        }

        throw new RecordNotFoundException("Unknown user type");
    }

    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public void reactivateUser(ReactivateUserDTO reactivateUserDTO) {
        User user = userRepository.findByEmail(reactivateUserDTO.getEmail())
                .orElseThrow(() -> new RecordNotFoundException("User with email '" + reactivateUserDTO.getEmail() + "' not found"));

        if (!user.getIsDeleted()) {
            throw new ConflictException("User with email '" + reactivateUserDTO.getEmail() + "' still exists");
        }

        if (!"123456".equals(reactivateUserDTO.getVerificationCode())) {
            throw new InvalidVerificationException();
        }

        user.setIsDeleted(false);
        user.setDeletedAt(null);

        userRepository.save(user);
    }

}
