package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.models.User;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> filterUsers(
            String userType,
            String firstName,
            String lastName,
            Boolean isDeleted,
            LocalDate deletedAfter,
            LocalDate deletedBefore
    ) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userType != null) {
                predicates.add(cb.equal(root.type(), userType.equalsIgnoreCase("EMPLOYEE") ? Employee.class : Customer.class));
            }

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("firstName"), firstName, false);

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("lastName"), lastName, false);

            SpecificationUtils.addDeletePredicates(predicates, cb, root.get("isDeleted"), isDeleted,
                    root.get("deletedAt"), deletedBefore, deletedAfter);

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
