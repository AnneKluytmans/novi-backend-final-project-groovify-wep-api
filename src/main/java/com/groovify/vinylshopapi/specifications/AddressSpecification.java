package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Address;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Employee;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AddressSpecification {
    public static Specification<Address> filterAddresses(
            Long addressId,
            Long customerId,
            Long employeeId,
            String userType,
            Boolean inactiveUsers,
            Boolean isShipping,
            Boolean isBilling,
            String country,
            String city,
            String postalCode
    ) {
        return (Root<Address> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
          List<Predicate> predicates = new ArrayList<>();

          if (addressId != null) {
              predicates.add(cb.equal(root.get("id"), addressId));
          }

          if (customerId != null) {
              predicates.add(cb.equal(root.get("customer").get("id"), customerId));
          }

          if (employeeId != null) {
              predicates.add(cb.equal(root.get("employee").get("id"), employeeId));
          }

          if (userType != null) {
              if ("customer".equalsIgnoreCase(userType)) {
                  predicates.add(cb.isNotNull(root.get("customer")));
              }
              if ("employee".equalsIgnoreCase(userType)) {
                  predicates.add(cb.isNotNull(root.get("employee")));
              }
          }

          Join<Address, Customer> customerJoin = root.join("customer", JoinType.LEFT);
          Join<Address, Employee> employeeJoin = root.join("employee", JoinType.LEFT);

          Predicate customerCondition;
          Predicate employeeCondition;

          if (inactiveUsers != null && inactiveUsers) {
              customerCondition = cb.and(
                      cb.isNotNull(root.get("customer")),
                      cb.equal(customerJoin.get("isDeleted"), true)
              );
              employeeCondition = cb.and(
                      cb.isNotNull(root.get("employee")),
                      cb.equal(employeeJoin.get("isDeleted"), true)
              );
              predicates.add(cb.or(customerCondition, employeeCondition));
          } else {
              customerCondition = cb.and(
                      cb.isNotNull(root.get("customer")),
                      cb.equal(customerJoin.get("isDeleted"), false)
              );
              employeeCondition = cb.and(
                      cb.isNotNull(root.get("employee")),
                      cb.equal(employeeJoin.get("isDeleted"), false)
              );
              predicates.add(cb.or(customerCondition, employeeCondition));
          }

          if (isShipping != null) {
              predicates.add(cb.and(cb.isNotNull(root.get("customer")), cb.equal(root.get("isShippingAddress"), isShipping)));
          }

          if (isBilling != null) {
              predicates.add(cb.and(cb.isNotNull(root.get("customer")), cb.equal(root.get("isBillingAddress"), isBilling)));
          }

          if (country != null && !country.isBlank()) {
              predicates.add(cb.like(cb.lower(root.get("country")), "%" + country.toLowerCase() + "%"));
          }

          if (city != null && !city.isBlank()) {
              predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
          }

          if (postalCode != null && !postalCode.isBlank()) {
              predicates.add(cb.like(cb.lower(root.get("postalCode")), "%" + postalCode.replace(" ", "").toLowerCase() + "%"));
          }

          return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
