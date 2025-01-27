package com.groovify.vinylshopapi.enums;

public enum SortOrder {
    ASC, DESC;

    public static SortOrder stringToSortOrder(String value) {
        if (value == null || value.isEmpty()) {
            return ASC;
        }
        switch (value.toUpperCase()) {
            case "ASC":
                return ASC;
            case "DESC":
                return DESC;
            default:
                throw new IllegalArgumentException("Invalid SortOrder value. Allowed values are 'ASC' or 'DESC'.");
        }
    }
}
