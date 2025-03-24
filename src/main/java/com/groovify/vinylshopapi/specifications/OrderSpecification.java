package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Order;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    public static Specification<Order> filterOrders(
            String confirmationStatus,
            String paymentStatus,
            String shippingStatus,
            List<String> excludedShippingStatuses,
            LocalDate orderedBefore,
            LocalDate orderedAfter,
            BigDecimal minTotalPrice,
            BigDecimal maxTotalPrice,
            Boolean isDeleted,
            LocalDate deletedAfter,
            LocalDate deletedBefore
    ) {
        return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("confirmationStatus"), confirmationStatus, false);
            SpecificationUtils.addStringPredicate(predicates, cb, root.get("paymentStatus"), paymentStatus, false);
            SpecificationUtils.addStringPredicate(predicates, cb, root.get("shippingStatus"), shippingStatus, false);

            if (excludedShippingStatuses != null && !excludedShippingStatuses.isEmpty()) {
                predicates.add(root.get("shippingStatus").in(excludedShippingStatuses).not());
            }

            SpecificationUtils.addDatePredicate(predicates, cb, root.get("orderDate"), orderedBefore, false);
            SpecificationUtils.addDatePredicate(predicates, cb, root.get("orderDate"), orderedAfter, true);

            if (minTotalPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("subTotalPrice"), minTotalPrice));
            }

            if (maxTotalPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("subTotalPrice"), maxTotalPrice));
            }

            SpecificationUtils.addDeletePredicates(predicates, cb, root.get("isDeleted"), isDeleted,
                    root.get("deletedAt"), deletedBefore, deletedAfter);

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
