package com.groovify.vinylshopapi.utils;

import org.springframework.data.domain.Sort;

import java.util.List;

public class SortHelper {

    public static Sort getSort(String sortBy, String sortOrder, List<String> allowedSortOptions) {
        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "id";
        } else {
            String normalizedSortBy = sortBy.trim().replace(" ", "").toLowerCase();
            sortBy = allowedSortOptions.stream()
                    .filter(sortOption -> sortOption.toLowerCase().equals(normalizedSortBy))
                    .findFirst()
                    .orElse("id");
        }

        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;

        return Sort.by(direction, sortBy);
    }
}
