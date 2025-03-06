package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.ReactivateUserDTO;
import com.groovify.vinylshopapi.dtos.UserResponseDTO;
import com.groovify.vinylshopapi.dtos.UserSummaryResponseDTO;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.InvalidVerificationException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CustomerMapper;
import com.groovify.vinylshopapi.mappers.EmployeeMapper;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.models.User;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.repositories.UserRepository;
import com.groovify.vinylshopapi.specifications.UserSpecification;

import com.groovify.vinylshopapi.utils.SortHelper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerMapper customerMapper;
    private final EmployeeMapper employeeMapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, CustomerMapper customerMapper, EmployeeMapper employeeMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerMapper = customerMapper;
        this.employeeMapper = employeeMapper;
    }


    public List<UserSummaryResponseDTO> getUsers(
            String userType,
            String firstName,
            String lastName,
            Boolean isDeleted,
            String deletedAfter,
            String deletedBefore,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = SortHelper.getSort(sortBy, sortOrder, List.of("id", "lastName", "email"));
        Specification<User> specification = UserSpecification.filterUsers(
                userType, firstName, lastName, isDeleted, deletedAfter, deletedBefore
        );
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


    public List<RoleType> getUserRoles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + userId + " not found"));

        Set<Role> userRoles = user.getRoles();

        List<RoleType> roles = new ArrayList<>();
        for (Role role : userRoles) {
            roles.add(role.getRoleType());
        }

        return roles;
    }

    public void addRolesToUser(Long userId, List<String> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + userId + " not found"));

        if (user instanceof Customer) {
            for (String role : roles) {
                if (role.equalsIgnoreCase("EMPLOYEE") || role.equalsIgnoreCase("ADMIN")) {
                    throw new IllegalArgumentException("Customers cannot be assigned employee or admin roles");
                }
            }
        }

        for (String role : roles) {
            RoleType roleType = RoleType.stringToRole(role);
            Role newRole = roleRepository.findByRoleType(roleType)
                    .orElseThrow(() -> new RecordNotFoundException("Role " + role + " not found"));

            user.getRoles().add(newRole);
        }

        userRepository.save(user);
    }

    public void removeRolesFromUser(Long userId, List<String> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + userId + " not found"));

        for (String role : roles) {
            RoleType roleType = RoleType.stringToRole(role);

            if (user instanceof Customer && role.equalsIgnoreCase("USER")) {
                throw new IllegalArgumentException("The role USER is mandatory for customers and cannot be removed from customer users.");
            }

            if (user instanceof Employee && role.equalsIgnoreCase("EMPLOYEE")) {
                throw new IllegalArgumentException("The role EMPLOYEE is mandatory for employees and cannot be removed from employee users.");
            }

            Role removeRole = roleRepository.findByRoleType(roleType)
                    .orElseThrow(() -> new RecordNotFoundException("Role " + role + " not found"));

            user.getRoles().remove(removeRole);
        }

        userRepository.save(user);
    }

}
