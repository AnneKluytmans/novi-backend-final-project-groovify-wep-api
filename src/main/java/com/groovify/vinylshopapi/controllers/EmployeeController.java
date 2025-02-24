package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.EmployeeAdminPatchDTO;
import com.groovify.vinylshopapi.dtos.EmployeeRegisterDTO;
import com.groovify.vinylshopapi.dtos.EmployeeResponseDTO;
import com.groovify.vinylshopapi.dtos.UserPatchDTO;
import com.groovify.vinylshopapi.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping
    public ResponseEntity<?> registerEmployee(@Valid @RequestBody EmployeeRegisterDTO employeeRegisterDTO,
                                              BindingResult bindingResult) {
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

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id,
                                            @Valid @RequestBody UserPatchDTO employeePatchDTO,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(id, employeePatchDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PatchMapping("/{id}/admin")
    public ResponseEntity<?> updateEmployeeByAdmin(@PathVariable Long id,
                                                   @Valid @RequestBody EmployeeAdminPatchDTO employeeAdminPatchDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployeeByAdmin(id, employeeAdminPatchDTO);
        return ResponseEntity.ok(updatedEmployee);
    }
}
