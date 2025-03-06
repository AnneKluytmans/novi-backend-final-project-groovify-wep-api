package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Address;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> filterCustomers(
            String firstName,
            String lastName,
            Boolean newsletterSubscribed,
            String country,
            String city,
            String postalCode,
            String houseNumber
    ) {
        return (Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Customer, Address> addressJoin = root.join("addresses", JoinType.LEFT);

            predicates.add(cb.equal(root.get("isDeleted"), false));

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("firstName"), firstName, false);

            SpecificationUtils.addStringPredicate(predicates, cb, root.get("lastName"), lastName, false);

            SpecificationUtils.addStringPredicate(predicates, cb, addressJoin.get("country"), country, false);

            SpecificationUtils.addStringPredicate(predicates, cb, addressJoin.get("city"), city, false);

            SpecificationUtils.addStringPredicate(predicates, cb, addressJoin.get("postalCode"), postalCode, true);

            SpecificationUtils.addStringPredicate(predicates, cb, addressJoin.get("houseNumber"), houseNumber, true);

            if (newsletterSubscribed != null) {
                predicates.add(cb.equal(root.get("isNewsletterSubscribed"), newsletterSubscribed));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
