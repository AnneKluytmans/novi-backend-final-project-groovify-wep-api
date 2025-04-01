package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.ChangePasswordDTO;
import com.groovify.vinylshopapi.dtos.UserResponseDTO;
import com.groovify.vinylshopapi.dtos.UserSummaryResponseDTO;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.exceptions.InvalidPasswordException;
import com.groovify.vinylshopapi.exceptions.PasswordConfirmationException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final CustomerMapper customerMapper;
    private final EmployeeMapper employeeMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            CustomerMapper customerMapper,
            EmployeeMapper employeeMapper,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.customerMapper = customerMapper;
        this.employeeMapper = employeeMapper;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<UserSummaryResponseDTO> getUsers(
            String userType,
            String firstName,
            String lastName,
            Boolean isDeleted,
            LocalDate deletedAfter,
            LocalDate deletedBefore,
            String sortBy,
            String sortOrder,
            Integer limit
    ) {
        Sort sort = SortHelper.getSort(sortBy, sortOrder, List.of("id", "lastName", "email"));
        Specification<User> specification = UserSpecification.filterUsers(
                userType, firstName, lastName, isDeleted, deletedAfter, deletedBefore
        );
        List<User> users = userRepository.findAll(specification, sort);
        if (limit != null && limit > 0 && limit < users.size()) {
            users = users.subList(0, limit);
        }

        List<UserSummaryResponseDTO> userSummaryResponseDTOs = new ArrayList<>();

        for (User user : users) {
            userSummaryResponseDTOs.add(mapToUserSummaryDTO(user));
        }

        return userSummaryResponseDTOs;
    }

    public UserResponseDTO getUserById(Long id) {
        return mapToUserResponseDTO(findUser(id, null));
    }

    public void deactivateUser(Long id) {
        User user = findUser(id, false);
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public void reactivateUser(Long id) {
        User user = findUser(id, true);
        user.setIsDeleted(false);
        user.setDeletedAt(null);

        userRepository.save(user);
    }

    public void changePassword(Long id, ChangePasswordDTO changePasswordDTO) {
        User user = findUser(id, false);

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Incorrect old password. You must confirm your current password to change it.");
        }
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new PasswordConfirmationException("Password change failed. New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    public List<RoleType> getUserRoles(Long userId) {
        Set<Role> userRoles = findUser(userId, null).getRoles();

        List<RoleType> roles = new ArrayList<>();
        for (Role role : userRoles) {
            roles.add(role.getRoleType());
        }

        return roles;
    }

    public void addRolesToUser(Long userId, List<RoleType> roles) {
        User user = findUser(userId, false);
        validateRolesForUserType(user, roles, true);

        for (RoleType role : roles) {
            user.getRoles().add(findRoleByType(role));
        }

        userRepository.save(user);
    }

    public void removeRolesFromUser(Long userId, List<RoleType> roles) {
        User user = findUser(userId, null);
        validateRolesForUserType(user, roles, false);

        for (RoleType role : roles) {
            user.getRoles().remove(findRoleByType(role));
        }

        userRepository.save(user);
    }


    private User findUser(Long userId, Boolean isDeleted) {
        if (isDeleted != null) {
            if (isDeleted) {
                return userRepository.findByIdAndIsDeletedTrue(userId)
                        .orElseThrow(() -> new RecordNotFoundException("No deactivated user found with id" + userId));
            } else {
                return userRepository.findByIdAndIsDeletedFalse(userId)
                        .orElseThrow(() -> new RecordNotFoundException("No user found with id" + userId));
            }
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("No user found with id: " + userId));
    }

    private Role findRoleByType(RoleType role) {
        return roleRepository.findByRoleType(role)
                .orElseThrow(() -> new RecordNotFoundException("Role " + role + " not found."));
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
                if (role == RoleType.USER) {
                    throw new IllegalArgumentException("The role USER is mandatory for all users and cannot be removed.");
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
