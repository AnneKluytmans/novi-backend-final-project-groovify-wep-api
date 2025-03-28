package com.groovify.vinylshopapi.enums;

public enum RoleType {
    USER,
    EMPLOYEE,
    ADMIN;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
