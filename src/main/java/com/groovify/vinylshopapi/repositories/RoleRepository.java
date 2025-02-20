package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
