package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByCustomerId(Long customerId);
    Optional<Address> findByEmployeeId(Long employeeId);
}
