package com.groovify.vinylshopapi.utils;

import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SpecificationUtils {
    public static void addStringPredicate(
            List<Predicate> predicates,
            CriteriaBuilder cb,
            Path<String> field,
            String value,
            Boolean removeSpaces
    ) {
        if (value != null && !value.isBlank()) {
            String formattedValue = removeSpaces ? value.replace(" ", "").toLowerCase() : value.toLowerCase();
            predicates.add(cb.like(cb.lower(field), "%" + formattedValue + "%"));
        }
    }

    public static void addDatePredicate(
            List<Predicate> predicates,
            CriteriaBuilder cb,
            Path<LocalDateTime> field,
            LocalDate dateValue,
            Boolean isAfter
    ) {
        if (dateValue != null) {
            if (isAfter) {
                predicates.add(cb.greaterThanOrEqualTo(field.as(LocalDate.class), dateValue));
            } else {
                predicates.add(cb.lessThanOrEqualTo(field.as(LocalDate.class), dateValue));
            }
        }
    }


    public static void addDeletePredicates(
            List<Predicate> predicates,
            CriteriaBuilder cb,
            Path<Boolean> isDeletedField,
            Boolean isDeleted,
            Path<LocalDateTime> deletedAtField,
            LocalDate deletedBefore,
            LocalDate deletedAfter
    ) {
       if (isDeleted != null) {
           predicates.add(cb.equal(isDeletedField, isDeleted));
       }

       addDatePredicate(predicates, cb, deletedAtField, deletedBefore, false);
       addDatePredicate(predicates, cb, deletedAtField, deletedAfter, true);
    }
}
