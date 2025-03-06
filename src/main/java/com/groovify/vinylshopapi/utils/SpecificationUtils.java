package com.groovify.vinylshopapi.utils;

import com.groovify.vinylshopapi.models.Address;
import jakarta.persistence.criteria.*;

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
}
