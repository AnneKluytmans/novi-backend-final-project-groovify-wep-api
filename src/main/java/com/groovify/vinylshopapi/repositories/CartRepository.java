package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
    Optional<Cart> findByCustomerIdAndCustomerIsDeletedFalse(Long customerId);
}
