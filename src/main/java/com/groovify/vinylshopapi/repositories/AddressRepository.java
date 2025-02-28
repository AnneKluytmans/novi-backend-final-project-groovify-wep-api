package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
