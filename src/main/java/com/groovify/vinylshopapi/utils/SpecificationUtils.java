package com.groovify.vinylshopapi.utils;

import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class SpecificationUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
            String dateValue,
            Boolean isAfter
    ) {
        if (dateValue != null && !dateValue.isBlank()) {
            try {
                LocalDate parsedDate = LocalDate.parse(dateValue, DATE_FORMATTER);
                if (isAfter) {
                    predicates.add(cb.greaterThanOrEqualTo(field.as(LocalDate.class), parsedDate));
                } else {
                    predicates.add(cb.lessThanOrEqualTo(field.as(LocalDate.class), parsedDate));
                }
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format: " + dateValue + ". Use format: dd-MM-yyyy (e.g., 12-02-2025)");
            }
        }
    }
}
