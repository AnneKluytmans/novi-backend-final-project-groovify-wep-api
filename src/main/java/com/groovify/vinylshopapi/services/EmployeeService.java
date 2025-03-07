package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.EmployeeMapper;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.repositories.EmployeeRepository;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.specifications.EmployeeSpecification;
import com.groovify.vinylshopapi.utils.SortHelper;
import com.groovify.vinylshopapi.validation.ValidationUtils;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final RoleRepository roleRepository;
    private final ValidationUtils validationUtils;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
                           RoleRepository roleRepository, ValidationUtils validationUtils) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.roleRepository = roleRepository;
        this.validationUtils = validationUtils;
    }


    public List<UserSummaryResponseDTO> getEmployees(
            String firstName,
            String lastName,
            String jobTitle,
            Double minSalary,
            Double maxSalary,
            String country,
            String city,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = SortHelper.getSort(sortBy, sortOrder, List.of("id", "lastName", "salary", "workHours"));
        Specification<Employee> specification = EmployeeSpecification.filterEmployees(
                firstName, lastName, jobTitle, minSalary, maxSalary, country, city
        );
        List<Employee> employees = employeeRepository.findAll(specification, sort);

        return employeeMapper.toUserSummaryResponseDTOs(employees);
    }

    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RecordNotFoundException("Employee with id " + id + " not found."));

        return employeeMapper.toResponseDTO(employee);
    }

    public EmployeeResponseDTO getEmployeeByUsername(String username) {
        Employee employee = employeeRepository.findByUsernameIgnoreCaseAndIsDeletedFalse(username)
                .orElseThrow(() -> new RecordNotFoundException("Employee with username " + username + " not found."));

        return employeeMapper.toResponseDTO(employee);
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
        }

        if (employeePatchDTO.getEmail() != null) {
            validationUtils.validateUniqueEmail(employeePatchDTO.getEmail(), employee.getId());
        }

        employeeMapper.partialUpdateEmployee(employeePatchDTO, employee);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(savedEmployee);
    }

    public EmployeeResponseDTO updateEmployeeByAdmin(Long id, EmployeeAdminPatchDTO employeePatchDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Employee with id " + id + " not found."));

        employeeMapper.partialUpdateEmployeeByAdmin(employeePatchDTO, employee);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(savedEmployee);
    }

}
