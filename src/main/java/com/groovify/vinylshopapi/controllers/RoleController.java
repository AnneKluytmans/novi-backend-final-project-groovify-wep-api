package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity<Set<RoleType>> getRoles() {
        Set<RoleType> roles = roleService.getRoles();
        return ResponseEntity.ok(roles);
    }

}
