package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.models.Role;
import com.groovify.vinylshopapi.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<RoleType> getRoles() {
        Set<RoleType> roles = new HashSet<>();
        for (Role role : roleRepository.findAll()) {
            roles.add(role.getRoleType());
        }

        return roles;
    }
}
