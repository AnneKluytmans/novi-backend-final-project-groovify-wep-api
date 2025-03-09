package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.models.User;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> filterUsers(
            String userType,
            String firstName,
            String lastName,
            Boolean isDeleted,
            String deletedAfter,
            String deletedBefore
    ) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            if (userType != null) {
                predicates.add(cb.equal(root.type(), userType.equalsIgnoreCase("EMPLOYEE") ? Employee.class : Customer.class));
            }

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("firstName"), firstName, false);

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("lastName"), lastName, false);

            if (isDeleted != null) {
                predicates.add(cb.equal(root.get("isDeleted"), isDeleted));
            }

            if (deletedAfter != null && !deletedAfter.isBlank()) {
                try {
                    LocalDate deletedAfterDate = LocalDate.parse(deletedAfter, formatter);
                    predicates.add(cb.greaterThanOrEqualTo(root.get("deletedAt").as(LocalDate.class), deletedAfterDate));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format for deletedAfter. Use format: dd-MM-yyyy (e.g., 12-02-2025)");
                }
            }

            if (deletedBefore != null && !deletedBefore.isBlank()) {
                try {
                    LocalDate deletedBeforeDate = LocalDate.parse(deletedBefore, formatter);
                    predicates.add(cb.lessThanOrEqualTo(root.get("deletedAt").as(LocalDate.class), deletedBeforeDate));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format for deletedBefore. Use format: dd-MM-yyyy (e.g., 12-02-2025)");
                }
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
