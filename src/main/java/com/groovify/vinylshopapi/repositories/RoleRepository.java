package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.enums.RoleType;
import com.groovify.vinylshopapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
