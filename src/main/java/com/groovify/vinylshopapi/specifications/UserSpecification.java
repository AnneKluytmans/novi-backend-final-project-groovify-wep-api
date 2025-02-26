package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.User;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> filterUsers(String userType, Boolean isDeleted) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userType != null) {
                if (!userType.equalsIgnoreCase("CUSTOMER") && !userType.equalsIgnoreCase("EMPLOYEE")) {
                    throw new IllegalArgumentException("Invalid user type: " + userType + ". Must be customer or employee");
                }

                predicates.add(cb.equal(root.get("userType"), userType.toUpperCase()));
            }

            if (isDeleted != null) {
                predicates.add(cb.equal(root.get("isDeleted"), isDeleted));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
