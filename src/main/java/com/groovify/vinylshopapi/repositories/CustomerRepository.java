package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByIdAndIsDeletedFalse(Long id);
    Optional<Customer> findByUsernameIgnoreCaseAndIsDeletedFalse(String username);
}
