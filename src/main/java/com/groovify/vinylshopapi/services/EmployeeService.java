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

    public EmployeeService(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper,
            RoleRepository roleRepository,
            ValidationUtils validationUtils
    ) {
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
        return employeeMapper.toResponseDTO(findEmployee(id));
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

        employee.getRoles().add(findRoleByRoleType(RoleType.EMPLOYEE));

        if (employeeRegisterDTO.getIsAdmin()) {
            employee.getRoles().add(findRoleByRoleType(RoleType.ADMIN));
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(savedEmployee);
    }

    public EmployeeResponseDTO updateEmployee(Long id, UserUpdateDTO employeeUpdateDTO) {
        Employee employee = findEmployee(id);

        validationUtils.validateUniqueUsername(employeeUpdateDTO.getUsername(), id);
        validationUtils.validateUniqueEmail(employeeUpdateDTO.getEmail(), id);

        employeeMapper.updateEmployee(employeeUpdateDTO, employee);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(savedEmployee);
    }

    public EmployeeResponseDTO updateEmployeeByAdmin(Long id, EmployeeAdminUpdateDTO employeeUpdateDTO) {
        Employee employee = findEmployee(id);

        employeeMapper.updateEmployeeByAdmin(employeeUpdateDTO, employee);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(savedEmployee);
    }


    private Employee findEmployee(Long id) {
        return employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RecordNotFoundException("Employee with id " + id + " not found."));
    }

    private Role findRoleByRoleType(RoleType roleType) {
        return roleRepository.findByRoleType(roleType)
                .orElseThrow(() -> new RecordNotFoundException("Role '" + roleType + "' not found."));
    }

}
