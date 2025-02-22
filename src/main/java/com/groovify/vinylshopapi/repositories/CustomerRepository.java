package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Boolean existsByEmail(String email);
}
