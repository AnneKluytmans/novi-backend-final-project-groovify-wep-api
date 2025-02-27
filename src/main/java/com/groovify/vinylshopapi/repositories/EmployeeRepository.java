package com.groovify.vinylshopapi.repositories;

import com.groovify.vinylshopapi.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByIdAndIsDeletedFalse(Long id);
    Optional<Employee> findByUsernameIgnoreCaseAndIsDeletedFalse(String username);
}
