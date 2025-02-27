package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.enums.SortOrder;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.mappers.EmployeeMapper;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.repositories.EmployeeRepository;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import com.groovify.vinylshopapi.specifications.EmployeeSpecification;
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


    public List<UserSummaryResponseDTO> getEmployees(String firstName, String lastName, String jobTitle, Double minSalary,
                                                     Double maxSalary, String sortBy, String sortOrder) {
        Sort sort = switch (sortBy.trim().toLowerCase()) {
            case "id" -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("id") : Sort.Order.asc("id"));
            case "salary" -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("salary") : Sort.Order.asc("salary"));
            case "workhours" -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("workHours") : Sort.Order.asc("workHours"));
            default -> Sort.by(SortOrder.stringToSortOrder(sortOrder) == SortOrder.DESC ? Sort.Order.desc("lastName") : Sort.Order.asc("lastName"));
        };

        Specification<Employee> specification = EmployeeSpecification.filterEmployees(firstName, lastName, jobTitle, minSalary, maxSalary);
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
