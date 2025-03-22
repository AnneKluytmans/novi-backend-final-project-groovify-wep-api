package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndIsDeletedFalse(Long orderId);
    Optional<Order> findByIdAndIsDeletedTrue(Long orderId);
}
