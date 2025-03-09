package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping
    public ResponseEntity<List<UserSummaryResponseDTO>> getEmployees(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        List<UserSummaryResponseDTO> employees = employeeService.getEmployees(
                firstName, lastName, jobTitle, minSalary, maxSalary,
                country, city, sortBy, sortOrder
        );
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(
            @PathVariable Long id
    ) {
        EmployeeResponseDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByUsername(
            @PathVariable String username
    ) {
        EmployeeResponseDTO employee = employeeService.getEmployeeByUsername(username);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<?> registerEmployee(
            @Valid @RequestBody EmployeeRegisterDTO employeeRegisterDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        EmployeeResponseDTO newEmployee = employeeService.registerEmployee(employeeRegisterDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newEmployee.getId())
                .toUri();

        return ResponseEntity.created(location).body(newEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO employeeUpdateDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(id, employeeUpdateDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PutMapping("/{id}/admin")
    public ResponseEntity<?> updateEmployeeByAdmin(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeAdminUpdateDTO employeeAdminUpdateDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployeeByAdmin(id, employeeAdminUpdateDTO);
        return ResponseEntity.ok(updatedEmployee);
    }
}
