package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.EmployeeAdminPatchDTO;
import com.groovify.vinylshopapi.dtos.EmployeeRegisterDTO;
import com.groovify.vinylshopapi.dtos.EmployeeResponseDTO;
import com.groovify.vinylshopapi.dtos.UserPatchDTO;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.EmployeeMapper;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.repositories.EmployeeRepository;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.repositories.UserRepository;
import com.groovify.vinylshopapi.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ValidationUtils validationUtils;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, UserRepository userRepository,
                           RoleRepository roleRepository, ValidationUtils validationUtils) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.validationUtils = validationUtils;
    }

    public EmployeeResponseDTO registerEmployee(EmployeeRegisterDTO employeeRegisterDTO) {
        Employee employee = employeeMapper.toEntity(employeeRegisterDTO);

        validationUtils.validateUniqueUsername(employeeRegisterDTO.getUsername(), employee.getId());
        validationUtils.validateUniqueEmail(employeeRegisterDTO.getEmail(), employee.getId());

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

    public EmployeeResponseDTO updateEmployee(Long id, UserPatchDTO employeePatchDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Employee with id " + id + " not found."));

        if (employeePatchDTO.getUsername() != null) {
            validationUtils.validateUniqueUsername(employeePatchDTO.getUsername(), employee.getId());
            employee.setUsername(employeePatchDTO.getUsername());
        }

        if (employeePatchDTO.getEmail() != null) {
            validationUtils.validateUniqueEmail(employeePatchDTO.getEmail(), employee.getId());
            employee.setEmail(employeePatchDTO.getEmail());
        }

        if (employeePatchDTO.getFirstName() != null && !employeePatchDTO.getFirstName().trim().isEmpty()) {
            employee.setFirstName(employeePatchDTO.getFirstName());
        }

        if (employeePatchDTO.getLastName() != null && !employeePatchDTO.getLastName().trim().isEmpty()) {
            employee.setLastName(employeePatchDTO.getLastName());
        }

        if (employeePatchDTO.getDateOfBirth() != null) {
            employee.setDateOfBirth(employeePatchDTO.getDateOfBirth());
        }

        if (employeePatchDTO.getPhone() != null) {
            employee.setPhone(employeePatchDTO.getPhone());
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(savedEmployee);
    }

    public EmployeeResponseDTO updateEmployeeByAdmin(Long id, EmployeeAdminPatchDTO employeePatchDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Employee with id " + id + " not found."));

        if (employeePatchDTO.getJobTitle() != null) {
            employee.setJobTitle(employeePatchDTO.getJobTitle());
        }

        if (employeePatchDTO.getSalary() != null) {
            employee.setSalary(employeePatchDTO.getSalary());
        }

        if (employeePatchDTO.getWorkHours() != null) {
            employee.setWorkHours(employeePatchDTO.getWorkHours());
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(savedEmployee);
    }

}
