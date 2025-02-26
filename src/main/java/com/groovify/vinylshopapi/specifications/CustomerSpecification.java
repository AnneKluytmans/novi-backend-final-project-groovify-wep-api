package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Customer;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> filterCustomers(String firstName, String lastName, Boolean newsletterSubscribed) {
        return (Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("isDeleted"), false));

            if (firstName != null && !firstName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
            }

            if (lastName != null && !lastName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
            }

            if (newsletterSubscribed != null) {
                predicates.add(cb.equal(root.get("isNewsletterSubscribed"), newsletterSubscribed));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
