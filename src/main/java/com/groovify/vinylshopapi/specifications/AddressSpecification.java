package com.groovify.vinylshopapi.specifications;

import com.groovify.vinylshopapi.models.Address;
import com.groovify.vinylshopapi.models.Customer;
import com.groovify.vinylshopapi.models.Employee;
import com.groovify.vinylshopapi.utils.SpecificationUtils;
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
            Boolean isStandAlone,
            String country,
            String city,
            String postalCode
    ) {
        return (Root<Address> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
          List<Predicate> predicates = new ArrayList<>();

          Join<Address, Customer> customerJoin = root.join("customer", JoinType.LEFT);
          Join<Address, Employee> employeeJoin = root.join("employee", JoinType.LEFT);

          if (addressId != null) {
              predicates.add(cb.equal(root.get("id"), addressId));
          }

          addUserConditions(customerId, employeeId, userType, inactiveUsers, predicates, cb, root, customerJoin, employeeJoin);

          if (isShipping != null) {
              predicates.add(cb.and(cb.isNotNull(root.get("customer")), cb.equal(root.get("isShippingAddress"), isShipping)));
          }

          if (isBilling != null) {
              predicates.add(cb.and(cb.isNotNull(root.get("customer")), cb.equal(root.get("isBillingAddress"), isBilling)));
          }

          if (isStandAlone != null) {
              Predicate noLinkedUser = cb.and(cb.isNull(root.get("customer")), cb.isNull(root.get("employee")));
              Predicate noLinkedOrder = cb.and(cb.isEmpty(root.get("shippingOrders")), cb.isEmpty(root.get("billingOrders")));
              Predicate standAloneCondition = cb.and(noLinkedUser, noLinkedOrder);

              if (isStandAlone) {
                  predicates.add(standAloneCondition);
              } else {
                  predicates.add(cb.not(standAloneCondition));
              }
          }

          SpecificationUtils.addStringPredicate(predicates, cb, root.get("country"), country, false);

          SpecificationUtils.addStringPredicate(predicates, cb, root.get("city"), city, false);

          SpecificationUtils.addStringPredicate(predicates, cb, root.get("postalCode"), postalCode, true);

          return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addUserConditions(
            Long customerId, Long employeeId, String userType, Boolean inactiveUsers,
            List<Predicate> predicates, CriteriaBuilder cb, Root<Address> root,
            Join<Address, Customer> customerJoin, Join<Address, Employee> employeeJoin
    ) {

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

        Boolean inactiveUserCondition = Boolean.TRUE.equals(inactiveUsers);

        Predicate customerCondition = cb.and(
                cb.isNotNull(root.get("customer")),
                cb.equal(customerJoin.get("isDeleted"), inactiveUserCondition)
            );

        Predicate employeeCondition = cb.and(
                cb.isNotNull(root.get("employee")),
                cb.equal(employeeJoin.get("isDeleted"), inactiveUserCondition)
            );

        Predicate standAloneCondition = cb.and(
                cb.isNull(root.get("customer")),
                cb.isNull(root.get("employee"))
            );

        predicates.add(cb.or(customerCondition, employeeCondition, standAloneCondition));
    }
}
