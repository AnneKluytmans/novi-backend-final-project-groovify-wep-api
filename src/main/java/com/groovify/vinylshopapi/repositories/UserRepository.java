package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndIsDeletedFalse(Long id);
    Optional<User> findByIdAndIsDeletedTrue(Long id);
}
