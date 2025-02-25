package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.ReactivateUserDTO;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.InvalidVerificationException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.CustomerMapper;
import com.groovify.vinylshopapi.mappers.EmployeeMapper;
import com.groovify.vinylshopapi.models.User;
import com.groovify.vinylshopapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
