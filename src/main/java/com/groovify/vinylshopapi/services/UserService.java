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

import java.time.LocalDate;
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

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CustomerMapper customerMapper,
            EmployeeMapper employeeMapper
    ) {
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
            LocalDate deletedAfter,
            LocalDate deletedBefore,
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
            userSummaryResponseDTOS.add(mapToUserSummaryDTO(user));
        }

        return userSummaryResponseDTOS;
    }

    public UserResponseDTO getUserById(Long id) {
        return mapToUserResponseDTO(findUser(id));
    }

    public void softDeleteUser(Long id) {
        User user = findUser(id);
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
        Set<Role> userRoles = findUser(userId).getRoles();

        List<RoleType> roles = new ArrayList<>();
        for (Role role : userRoles) {
            roles.add(role.getRoleType());
        }

        return roles;
    }

    public void addRolesToUser(Long userId, List<RoleType> roles) {
        User user = findUser(userId);
        validateRolesForUserType(user, roles, true);

        for (RoleType role : roles) {
            user.getRoles().add(findRoleByType(role));
        }

        userRepository.save(user);
    }

    public void removeRolesFromUser(Long userId, List<RoleType> roles) {
        User user = findUser(userId);
        validateRolesForUserType(user, roles, false);

        for (RoleType role : roles) {
            user.getRoles().remove(findRoleByType(role));
        }

        userRepository.save(user);
    }


    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + userId + " not found"));
    }

    private Role findRoleByType(RoleType role) {
        return roleRepository.findByRoleType(role)
                .orElseThrow(() -> new RecordNotFoundException("Role " + role + " not found"));
    }

    private void validateRolesForUserType(User user, List<RoleType> roles, Boolean isNewRole) {
        for (RoleType role : roles) {
            if (user instanceof Customer && (role == RoleType.EMPLOYEE || role == RoleType.ADMIN)) {
                throw new IllegalArgumentException("Customers cannot be assigned employee or admin roles");
            }
            if (!isNewRole) {
                if (user instanceof Employee && role == RoleType.EMPLOYEE) {
                    throw new IllegalArgumentException("The role EMPLOYEE is mandatory for employees and cannot be removed.");
                }
                if (user instanceof Customer && role == RoleType.USER) {
                    throw new IllegalArgumentException("The role USER is mandatory for customers and cannot be removed.");
                }
            }
        }
    }

    private UserSummaryResponseDTO mapToUserSummaryDTO(User user) {
        if (user instanceof Customer) {
            return customerMapper.toUserSummaryResponseDTO((Customer) user);
        } else if (user instanceof Employee) {
            return employeeMapper.toUserSummaryResponseDTO((Employee) user);
        }
        throw new RecordNotFoundException("Unknown user type");
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        if (user instanceof Customer) {
            return customerMapper.toUserResponseDTO((Customer) user);
        } else if (user instanceof Employee) {
            return employeeMapper.toUserResponseDTO((Employee) user);
        }
        throw new RecordNotFoundException("Unknown user type");
    }
}
