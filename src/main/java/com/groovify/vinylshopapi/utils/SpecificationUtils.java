package com.groovify.vinylshopapi.utils;

import jakarta.persistence.criteria.*;

import java.math.BigDecimal;
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

    public static void addBooleanPredicate(
            List<Predicate> predicates,
            CriteriaBuilder cb,
            Path<Boolean> field,
            Boolean value
    ) {
        if (value != null) {
            predicates.add(cb.equal(field, value));
        }
    }

    public static void addPricePredicates(
            List<Predicate> predicates,
            CriteriaBuilder cb,
            Path<BigDecimal> field,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(field, minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(field, maxPrice));
        }
    }

    public static void addDatePredicates(
            List<Predicate> predicates,
            CriteriaBuilder cb,
            Path<LocalDateTime> field,
            LocalDate dateValue,
            LocalDate dateBeforeValue,
            LocalDate dateAfterValue
    ) {
        if (dateValue != null) {
            predicates.add(cb.equal(field.as(LocalDate.class), dateValue));
        }

        if (dateBeforeValue != null) {
            predicates.add(cb.lessThanOrEqualTo(field.as(LocalDate.class), dateBeforeValue));
        }

        if (dateAfterValue != null) {
            predicates.add(cb.greaterThanOrEqualTo(field.as(LocalDate.class), dateAfterValue));
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
        addBooleanPredicate(predicates, cb, isDeletedField, isDeleted);
        addDatePredicates(predicates, cb, deletedAtField, null, deletedBefore, deletedAfter);
    }
}
