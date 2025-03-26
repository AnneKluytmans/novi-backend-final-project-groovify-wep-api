package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Address;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {
    public static Specification<Employee> filterEmployees(
            String firstName,
            String lastName,
            String jobTitle,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            String country,
            String city
    ) {
        return (Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Employee, Address> addressJoin = root.join("address", JoinType.LEFT);

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("firstName"), firstName, false);

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("lastName"), lastName, false);

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("jobTitle"), jobTitle, false);

            SpecificationUtils.addPricePredicates(predicates, cb, root.get("salary"), minSalary, maxSalary);

            SpecificationUtils.addStringPredicate(predicates, cb, addressJoin.get("country"), country, false);

            SpecificationUtils.addStringPredicate(predicates, cb, addressJoin.get("city"), city, false);

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
