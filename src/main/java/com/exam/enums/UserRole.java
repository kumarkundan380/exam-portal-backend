package com.exam.enums;

public enum UserRole {
    USER ("USER"),
    ADMIN ("ADMIN");

    private String value;

    public String getValue() {
        return value;
    }
    UserRole(String value){
        this.value = value;
    }
}
