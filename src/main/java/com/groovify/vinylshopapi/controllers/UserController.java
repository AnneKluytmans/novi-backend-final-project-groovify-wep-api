package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.ReactivateUserDTO;
import com.groovify.vinylshopapi.dtos.UserResponseDTO;
import com.groovify.vinylshopapi.dtos.UserSummaryResponseDTO;
import com.groovify.vinylshopapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserSummaryResponseDTO>> getAllUsers(
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) String userType,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder
    ) {
            List<UserSummaryResponseDTO> users = userService.getAllUsers(isDeleted, userType, sortBy, sortOrder);
            return ResponseEntity.ok(users);
    }


    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reactivate")
    public ResponseEntity<Void> reactivateUser(@Valid @RequestBody ReactivateUserDTO reactivateUserDTO) {
        userService.reactivateUser(reactivateUserDTO);
        return ResponseEntity.noContent().build();
    }
}
