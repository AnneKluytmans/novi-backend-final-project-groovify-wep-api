package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Cart;
import com.groovify.vinylshopapi.models.CartItem;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CartSpecification {
    public static Specification<Cart> filterCarts(
            String createdBefore,
            String createdAfter,
            String updatedBefore,
            String updatedAfter,
            Long customerId,
            Boolean isEmpty,
            Integer minAmountOfItems
    ) {
        return (Root<Cart> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            SpecificationUtils.addDatePredicate(predicates, cb, root.get("createdAt"), createdBefore, false);
            SpecificationUtils.addDatePredicate(predicates, cb, root.get("createdAt"), createdAfter, true);
            SpecificationUtils.addDatePredicate(predicates, cb, root.get("updatedAt"), updatedBefore, false);
            SpecificationUtils.addDatePredicate(predicates, cb, root.get("updatedAt"), updatedAfter, true);

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer"), customerId));
            }

            if (isEmpty != null) {
                if (isEmpty) {
                    predicates.add(cb.equal(cb.size(root.get("cartItems")), 0));
                } else {
                    predicates.add(cb.greaterThan(cb.size(root.get("cartItems")), 0));
                }
            }

            if (minAmountOfItems != null) {
                Join<Cart, CartItem> cartItemsJoin = root.join("cartItems", JoinType.LEFT);
                Expression<Integer> totalItems = cb.sum(cartItemsJoin.get("quantity"));
                predicates.add(cb.greaterThanOrEqualTo(totalItems, minAmountOfItems));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
