package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.EmployeeRegisterDTO;
import com.groovify.vinylshopapi.dtos.EmployeeResponseDTO;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.EmployeeMapper;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.repositories.EmployeeRepository;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, UserRepository userRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public EmployeeResponseDTO registerEmployee(EmployeeRegisterDTO employeeRegisterDTO) {
        Employee employee = employeeMapper.toEntity(employeeRegisterDTO);

        if (userRepository.existsByUsername(employee.getUsername().toLowerCase())) {
            throw new ConflictException("Username '" + employee.getUsername() + "' already in use. Please choose another one.");
        }

        if (userRepository.existsByEmail(employee.getEmail().toLowerCase())) {
            throw new ConflictException("Email '" + employee.getEmail() + "' already in use. Use another email.");
        }

        Role employeeRole = roleRepository.findByRoleType(RoleType.EMPLOYEE)
                .orElseThrow(() -> new RecordNotFoundException("Role '" + RoleType.EMPLOYEE + "' not found."));

        employee.getRoles().add(employeeRole);

        if (employeeRegisterDTO.getIsAdmin()) {
            Role adminRole = roleRepository.findByRoleType(RoleType.ADMIN)
                    .orElseThrow(() -> new RecordNotFoundException("Role '" + RoleType.ADMIN + "' not found."));
            employee.getRoles().add(adminRole);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(savedEmployee);
    }
}
