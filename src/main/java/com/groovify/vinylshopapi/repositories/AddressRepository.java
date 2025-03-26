package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    Optional<Address> findByEmployeeIdAndEmployeeIsDeletedFalse(Long employeeId);
    Optional<Address> findByIdAndCustomerIdAndCustomerIsDeletedFalse(Long addressId, Long customerId);

    Boolean existsByEmployeeId(Long employeeId);
}
