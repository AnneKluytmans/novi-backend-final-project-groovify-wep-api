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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final RoleRepository roleRepository;
    private final ValidationUtils validationUtils;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper,
            RoleRepository roleRepository,
            ValidationUtils validationUtils,
            PasswordEncoder passwordEncoder
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.roleRepository = roleRepository;
        this.validationUtils = validationUtils;
        this.passwordEncoder = passwordEncoder;
    }


    public List<UserSummaryResponseDTO> getEmployees(
            String firstName,
            String lastName,
            String jobTitle,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String country,
            String city,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = SortHelper.getSort(sortBy, sortOrder, List.of("id", "lastName", "salary", "workHours"));
        Specification<Employee> specification = EmployeeSpecification.filterEmployees(
                firstName, lastName, jobTitle, minSalary, maxSalary, country, city
        );
        return employeeMapper.toUserSummaryResponseDTOs(employeeRepository.findAll(specification, sort));
    }

    public EmployeeResponseDTO getEmployeeById(Long id) {
        return employeeMapper.toResponseDTO(findEmployee(id));
    }

    public EmployeeResponseDTO getEmployeeByUsername(String username) {
        Employee employee = employeeRepository.findByUsernameIgnoreCaseAndIsDeletedFalse(username)
                .orElseThrow(() -> new RecordNotFoundException("No employee found with username: " + username));

        return employeeMapper.toResponseDTO(employee);
    }

    public EmployeeResponseDTO registerEmployee(EmployeeRegisterDTO employeeRegisterDTO) {
        Employee employee = employeeMapper.toEntity(employeeRegisterDTO);

        validationUtils.validateUniqueUsername(employeeRegisterDTO.getUsername(), employee.getId());
        validationUtils.validateUniqueEmail(employeeRegisterDTO.getEmail(), employee.getId());

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        employee.getRoles().add(findRoleByRoleType(RoleType.USER));
        employee.getRoles().add(findRoleByRoleType(RoleType.EMPLOYEE));

        if (employeeRegisterDTO.getIsAdmin()) {
            employee.getRoles().add(findRoleByRoleType(RoleType.ADMIN));
        }

        return employeeMapper.toResponseDTO(employeeRepository.save(employee));
    }

    public EmployeeResponseDTO updateEmployee(Long id, UserUpdateDTO employeeUpdateDTO) {
        Employee employee = findEmployee(id);

        validationUtils.validateUniqueUsername(employeeUpdateDTO.getUsername(), id);
        validationUtils.validateUniqueEmail(employeeUpdateDTO.getEmail(), id);

        employeeMapper.updateEmployee(employeeUpdateDTO, employee);
        return employeeMapper.toResponseDTO(employeeRepository.save(employee));
    }

    public EmployeeResponseDTO updateEmployeeByAdmin(Long id, EmployeeAdminUpdateDTO employeeUpdateDTO) {
        Employee employee = findEmployee(id);
        employeeMapper.updateEmployeeByAdmin(employeeUpdateDTO, employee);

        return employeeMapper.toResponseDTO(employeeRepository.save(employee));
    }


    private Employee findEmployee(Long id) {
        return employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RecordNotFoundException("No employee found with id: " + id));
    }

    private Role findRoleByRoleType(RoleType roleType) {
        return roleRepository.findByRoleType(roleType)
                .orElseThrow(() -> new RecordNotFoundException("Role '" + roleType + "' not found."));
    }

}
