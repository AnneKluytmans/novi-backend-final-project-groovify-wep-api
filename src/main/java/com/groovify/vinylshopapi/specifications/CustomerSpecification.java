package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Address;
import com.groovify.vinylshopapi.models.Customer;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> filterCustomers(String firstName, String lastName, Boolean newsletterSubscribed,
                                                          String country, String city, String postalCode, String houseNumber) {
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

            Join<Customer, Address> addressJoin = root.join("addresses", JoinType.INNER);

            if (country != null && !country.isBlank()) {
                predicates.add(cb.like(cb.lower(addressJoin.get("country")), "%" + country.toLowerCase() + "%"));
            }

            if (city != null && !city.isBlank()) {
                predicates.add(cb.like(cb.lower(addressJoin.get("city")), "%" + city.toLowerCase() + "%"));
            }

            if (postalCode != null && !postalCode.isBlank()) {
                predicates.add(cb.like(cb.lower(addressJoin.get("postalCode")), "%" + postalCode.replace(" ", "").toLowerCase() + "%"));
            }

            if (houseNumber != null && !houseNumber.isBlank()) {
                predicates.add(cb.like(cb.lower(addressJoin.get("houseNumber")), houseNumber.replace(" ", "").toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
