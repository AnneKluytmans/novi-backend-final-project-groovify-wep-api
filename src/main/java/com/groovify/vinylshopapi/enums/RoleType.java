package com.groovify.vinylshopapi.enums;

import java.util.Arrays;

public enum RoleType {
    USER,
    EMPLOYEE,
    ADMIN;

    public static RoleType stringToRole(String roleType) {
        try {
            return RoleType.valueOf(roleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role type: " + roleType + ". Valid values are: " + Arrays.toString(RoleType.values()));
        }
    }
}
