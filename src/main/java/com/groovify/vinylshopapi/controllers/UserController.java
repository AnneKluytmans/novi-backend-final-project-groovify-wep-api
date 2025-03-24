package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.ReactivateUserDTO;
import com.groovify.vinylshopapi.dtos.UserResponseDTO;
import com.groovify.vinylshopapi.dtos.UserSummaryResponseDTO;
import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserSummaryResponseDTO>> getUsers(
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) String deletedAfter,
            @RequestParam(required = false) String deletedBefore,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
            List<UserSummaryResponseDTO> users = userService.getUsers(
                    userType, firstName, lastName, isDeleted, deletedAfter, deletedBefore,
                    sortBy, sortOrder
            );
            return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id
    ) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<Void> softDeleteUser(
            @PathVariable Long id
    ) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reactivate")
    public ResponseEntity<Void> reactivateUser(
            @Valid @RequestBody ReactivateUserDTO reactivateUserDTO
    ) {
        userService.reactivateUser(reactivateUserDTO);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleType>> getUserRoles(
            @PathVariable("id") Long userId
    ) {
        List<RoleType> roles = userService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<Void> addRolesToUser(
            @PathVariable("id") Long userId,
            @RequestBody List<RoleType> roles
    ) {
        userService.addRolesToUser(userId, roles);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/roles")
    public ResponseEntity<Void> removeRolesFromUser(
            @PathVariable("id") Long userId,
            @RequestBody List<RoleType> roles
    ) {
        userService.removeRolesFromUser(userId, roles);
        return ResponseEntity.noContent().build();
    }

}
