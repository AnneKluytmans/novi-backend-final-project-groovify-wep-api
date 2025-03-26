package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Cart;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CartSpecification {
    public static Specification<Cart> filterCarts(
            LocalDate createdBefore,
            LocalDate createdAfter,
            LocalDate updatedBefore,
            LocalDate updatedAfter,
            Long customerId,
            Boolean isEmpty
    ) {
        return (Root<Cart> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            SpecificationUtils.addDatePredicates(predicates, cb, root.get("createdAt"), null, createdBefore, createdAfter);

            SpecificationUtils.addDatePredicates(predicates, cb, root.get("updatedAt"), null, updatedBefore, updatedAfter);

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), customerId));
            }

            if (isEmpty != null) {
                if (isEmpty) {
                    predicates.add(cb.equal(cb.size(root.get("cartItems")), 0));
                } else {
                    predicates.add(cb.greaterThan(cb.size(root.get("cartItems")), 0));
                }
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
