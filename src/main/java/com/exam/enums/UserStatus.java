package com.exam.enums;

public enum UserStatus {
    ACTIVE ("ACTIVE"),
    DELETED ("DELETED");

    private String value;

    public String getValue() {
        return value;
    }
    UserStatus(String value){
        this.value = value;
    }

}
