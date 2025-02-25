package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.ReactivateUserDTO;
import com.groovify.vinylshopapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
