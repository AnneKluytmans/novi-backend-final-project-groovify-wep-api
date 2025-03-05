package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Address;
import com.groovify.vinylshopapi.models.Employee;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {
    public static Specification<Employee> filterEmployees(String firstName, String lastName, String jobTitle,
                                                          Double minSalary, Double maxSalary, String country, String city) {
        return (Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null && !firstName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
            }

            if (lastName != null && !lastName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
            }

            if (jobTitle != null && !jobTitle.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("jobTitle")), "%" + jobTitle.toLowerCase() + "%"));
            }

            if (minSalary != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salary"), minSalary));
            }

            if (maxSalary != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salary"), maxSalary));
            }

            Join<Employee, Address> addressJoin = root.join("address", JoinType.INNER);
            if (country != null && !country.isBlank()) {
                predicates.add(cb.like(cb.lower(addressJoin.get("country")), "%" + country.toLowerCase() + "%"));
            }

            if (city != null && !city.isBlank()) {
                predicates.add(cb.like(cb.lower(addressJoin.get("city")), "%" + city.toLowerCase() + "%"));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
